package app;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import model.MailRequest;

@Service
public class MailService {
    @Value("${mailgun.apiKey}")
    private String mailGunKey;

    @Value("${sendgrid.apiKey}")
    private String sendGridKey;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final String mailGunBaseUrl = "https://api.mailgun.net/v3/sandboxbeee9c1238f84a3296a5a9b08f7fa6cf.mailgun.org/messages";

    private final String sendGridBaseUrl = "https://api.sendgrid.com/v3/mail/send";

    public boolean mailGun(MailRequest request) {
        try {

            HttpResponse<JsonNode> jsonNode = Unirest.post(mailGunBaseUrl).basicAuth("api", mailGunKey)
                    .field("from", request.getFrom().getEmail()).field("to", request.convertEmailList(request.getTo()))
                    .field("cc", request.convertEmailList(request.getCc()))
                    .field("bcc", request.convertEmailList(request.getBcc()))
                    .field("subject", request.getSubject())
                    .field("text", request.getText()).asJson();

            int statusCode = jsonNode.getStatus();
            if (statusCode == 200 || statusCode == 202) {
                log.info("Successfully sent mail via mailGun details: {}", request);
                return true;
            }

            log.error("Invalid status code from mailGun: {}", String.valueOf(statusCode));
        } catch (UnirestException libErr) {
            log.error("Failed to send mailGun request: {}", libErr);
        }

        return false;
    }

    public boolean sendGrid(MailRequest request) {
        String jsonPayload = "";
        request.generatePersonalizations();
        try {
            ObjectWriter objectWriter = new ObjectMapper().writer();
            jsonPayload = objectWriter.writeValueAsString(request);
        } catch (JsonProcessingException err) {
            log.error("Failed to serialize sendGrid request into json: {}", err);
        }

        try {
            HttpResponse<JsonNode> jsonNode = Unirest.post(sendGridBaseUrl).header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + sendGridKey).body(jsonPayload).asJson();

            int statusCode = jsonNode.getStatus();
            if (statusCode == 200 || statusCode == 202) {
                log.info("Successfully sent mail via sendGrid details: {}", request);
                return true;
            }

            log.error("Invalid status code from sendGrid: {}", String.valueOf(statusCode));
        } catch (UnirestException libErr) {
            log.error("Failed to send sendGrid request: {}", libErr);
        }

        return false;
    }

    public boolean sendMail(MailRequest request) {
        if (!this.mailGun(request)) {
            log.warn("Failed to send email through mailgun.");
            return this.sendGrid(request);
        }
        return true;
    }
}