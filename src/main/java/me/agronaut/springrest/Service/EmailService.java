package me.agronaut.springrest.Service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService{

    @Autowired private JavaMailSender sender;

    @Value("${email.noreply}")
    public String NO_REPLY_ADDRESS;

    public void sendEmail(String from, String to, String subject, String msg) {
        //log.debug("email details:\n\t[Details]\t" + from + ", " + to + ", " + subject + ", " + msg);


        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setFrom((from == null ? NO_REPLY_ADDRESS : from));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(msg ,true);

            sender.send(message);
//            log.debug("Email sent to address: " + to);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
