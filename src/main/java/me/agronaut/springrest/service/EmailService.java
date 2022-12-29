package me.agronaut.springrest.service;

import me.agronaut.springrest.exception.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService{

    @Autowired private JavaMailSender sender;

    @Value("${email.noreply}")
    public String noReplyAddress;

    public void sendEmail(String from, String to, String subject, String msg) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setFrom((from == null ? noReplyAddress : from));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(msg ,true);

            sender.send(message);
        } catch (MessagingException e) {
            throw new EmailException("Failed to send email");
        }

    }
}
