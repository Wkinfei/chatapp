package nus.iss.chatapp.com.server.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
    
    @Autowired
    JavaMailSender javaMailSender;

    public void SendEmail(String name, String email, String content, String subject) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        String toEmail = "WhatsChat2023@gmail.com";
        String mailContent = "<p style=\"color: blue\"><b>Sender Email:<b> "+ email + "</p>";
        mailContent += "<p><b>Name:<b> "+ name + "</p>";
        mailContent += "<p><b>Content:<b> "+ content + "</p>";

        helper.setFrom("WhatsChat2023@gmail.com","WhatsChat FAQ");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        javaMailSender.send(message);
    
        System.out.println("Mail send" + helper.toString());
    }

}
