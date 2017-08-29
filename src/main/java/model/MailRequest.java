package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import cz.jirutka.validator.collection.constraints.EachPattern;
import lombok.Data;
import model.SendGridPersonalizations;
import model.SendGridContent;

@Data
public class MailRequest {

    @NotEmpty
    @JsonProperty(access = Access.WRITE_ONLY)
    @Valid
    public List<Email> to = Collections.emptyList();

    @JsonProperty(access = Access.WRITE_ONLY)
    @Valid
    public List<Email> cc = Collections.emptyList();

    @JsonProperty(access = Access.WRITE_ONLY)
    @Valid
    public List<Email> bcc = Collections.emptyList();

    @NotNull
    @Size(min = 1, max = 200)
    @JsonProperty(access = Access.WRITE_ONLY)
    public String text;

    @NotNull
    @Size(min = 1, max = 100)
    @JsonProperty(access = Access.WRITE_ONLY)
    public String subject;

    @NotNull
    @Valid
    public Email from;

    public List<SendGridPersonalizations> personalizations;
    public List<SendGridContent> content;

    public String convertEmailList(List<Email> emailList) {
        return emailList.stream().map((email) -> {
            return email.getEmail();
        }).collect(Collectors.joining(","));
    }

    public void generatePersonalizations() {
        SendGridPersonalizations personalization = new SendGridPersonalizations();
        personalization.setTo(this.to);

        if (this.cc.size() > 0) {
            personalization.setCc(this.cc);
        }
        if (this.bcc.size() > 0) {
            personalization.setBcc(this.bcc);
        }
        personalization.setSubject(this.subject);
        this.personalizations = Arrays.asList(personalization);
        this.content = Arrays.asList(new SendGridContent(this.text));
    }

    public Map<String, Object> generateMailGunFields() {
        Map<String, Object> fields = new HashMap<>();
        if (this.cc.size() > 0) {
            fields.put("cc", this.convertEmailList(this.cc));
        }
        if (this.bcc.size() > 0) {
            fields.put("bcc", this.convertEmailList(this.bcc));
        }
        fields.put("to", this.convertEmailList(this.to));
        fields.put("subject", this.subject);
        fields.put("text", this.text);
        fields.put("from", this.from.getEmail());
        return fields;
    }
}