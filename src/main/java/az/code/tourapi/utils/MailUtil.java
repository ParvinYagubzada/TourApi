package az.code.tourapi.utils;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class MailUtil {

    private final JavaMailSender mailSender;

    public MailUtil(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotificationEmail(String to, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        mailSender.send(mail);
    }

}
