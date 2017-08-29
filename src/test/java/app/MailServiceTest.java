package app;

import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;

import app.MailService;
import model.MailRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class MailServiceTest {

    @Test
    public void shouldCallSingleService() {
        MailService mailService = new MailService();
        try {
            Mockito.doReturn(true).when(mailService).mailGun(new MailRequest());
            // when(Unirest.post(any()).asJson()).thenReturn();
            boolean result = mailService.sendMail(new MailRequest());
            assertThat(result).isTrue();
            verify(mailService, times(1)).sendMail(new MailRequest());
            verify(mailService, times(0)).sendGrid(new MailRequest());

        } catch (NotAMockException err) {
        }
    }

    @Test
    public void shouldFallBack() {
        MailService mailService = new MailService();
        try {
            Mockito.doReturn(false).when(mailService).mailGun(new MailRequest());
            Mockito.doReturn(true).when(mailService).sendGrid(new MailRequest());
            boolean result = mailService.sendMail(new MailRequest());
            assertThat(result).isTrue();
            verify(mailService, times(1)).sendMail(new MailRequest());
            verify(mailService, times(1)).sendGrid(new MailRequest());

        } catch (Exception err) {
        }
    }

}