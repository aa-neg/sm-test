package app;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;
import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import model.MailRequest;
import model.Email;

@RunWith(SpringRunner.class)
public class MailRequestTest {
    private static Validator validator;

    private static Email validEmail = new Email("valid@valid.com");

    private static Email validEmail_2 = new Email("somethingelse@valid.com.au");

    private static Email invalidEmail = new Email("v@@@valid#$%#");

    @Before
    public void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testBasicInvalidEmails() throws Exception {
        MailRequest request = new MailRequest();

        request.setTo(Arrays.asList(invalidEmail, validEmail));
        request.setText("sometext");
        request.setSubject("somesubject");
        request.setCc(Arrays.asList(validEmail_2));
        request.setFrom(validEmail);

        Set<ConstraintViolation<MailRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void validateRequiredFields() throws Exception {
        MailRequest request = new MailRequest();

        request.setTo(Arrays.asList(validEmail_2, validEmail));
        request.setText("sometext");
        request.setSubject("somesubject");
        request.setFrom(validEmail);

        Set<ConstraintViolation<MailRequest>> violations = validator.validate(request);
        assertThat(violations.isEmpty()).isEqualTo(true);
    }

    @Test
    public void throwsInvalidOnMissingFields() throws Exception {
        MailRequest request = new MailRequest();

        request.setFrom(null);
        request.setTo(Collections.emptyList());
        request.setText(null);
        request.setSubject(null);

        Set<ConstraintViolation<MailRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(4);
    }

    @Test
    public void testArbritaryStringLengths() throws Exception {
        MailRequest request = new MailRequest();

        request.setFrom(invalidEmail);
        request.setTo(Collections.emptyList());
        request.setText(
                "alskdjlaksjdlkajslkdjalkdjlkdjalksjdlkasjdlkajdlkjsldjalksjdlakjdlksjlkajlksjdlkajsdlkajklsdjklasjdlkajdlkkkkkkkklkkjlajsdklajskldjasldjakljdklasjdlkajdlksajdklsajdkljdlkjsldjaldjskldjlkajlksjdlakdjlskjdlajdslkjaldskjadlkjsdlkjalkjalskdjlaksjdlkajslkdjalkdjlkdjalksjdlkasjdlkajdlkjsldjalksjdlakjdlksjlkajlksjdlkajsdlkajklsdjklasjdlkajdlkkkkkkkklkkjlajsdklajskldjasldjakljdklasjdlkajdlksajdklsajdkljdlkjsldjaldjskldjlkajlksjdlakdjlskjdlajdslkjaldskjadlkjsdlkjalkjalskdjlaksjdlkajslkdjalkdjlkdjalksjdlkasjdlkajdlkjsldjalksjdlakjdlksjlkajlksjdlkajsdlkajklsdjklasjdlkajdlkkkkkkkklkkjlajsdklajskldjasldjakljdklasjdlkajdlksajdklsajdkljdlkjsldjaldjskldjlkajlksjdlakdjlskjdlajdslkjaldskjadlkjsdlkjalkj");
        request.setSubject("validlength");

        Set<ConstraintViolation<MailRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(3);
    }
}