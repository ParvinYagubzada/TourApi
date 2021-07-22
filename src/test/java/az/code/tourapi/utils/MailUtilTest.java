package az.code.tourapi.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class MailUtilTest {

    private final JavaMailSender mailSender = mock(JavaMailSender.class);

    @Test
    @DisplayName("MailUtil - sendNotificationEmail")
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