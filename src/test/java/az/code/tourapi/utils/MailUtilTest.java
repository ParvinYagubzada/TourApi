package az.code.tourapi.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static az.code.tourapi.TourApiApplicationTests.TEST_STRING;
import static org.mockito.Mockito.*;

class MailUtilTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);

    @Test
    @DisplayName("MailUtil - sendNotificationEmail()")
    void sendNotificationEmail() {
        MailUtil util = new MailUtil(mailSender);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("me");
        mail.setSubject(TEST_STRING);
        mail.setText(TEST_STRING);
        doNothing().when(mailSender).send(mail);
        util.sendNotificationEmail("me", TEST_STRING, TEST_STRING);
        verify(mailSender, times(1)).send(mail);
    }
}