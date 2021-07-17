package az.code.tourapi.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class MailUtilTest {

    JavaMailSender mailSender = mock(JavaMailSender.class);

    @Test
    void sendNotificationEmail() {
        MailUtil util = new MailUtil(mailSender);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("me");
        mail.setSubject("test");
        mail.setText("test");
        doNothing().when(mailSender).send(mail);
        util.sendNotificationEmail("me", "test", "test");
        verify(mailSender, times(1)).send(mail);
    }
}