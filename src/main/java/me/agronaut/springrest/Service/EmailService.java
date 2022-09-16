package me.agronaut.springrest.Service;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
@Log4j2
public class EmailService {
    @Autowired
    private JavaMailSender sender;

    @Value("${email.noreply}")
    public static String NO_REPLY_ADDRESS;

    public void sendEmail(String from, String to, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);

        sender.send(message);
    }
}
