package nus.iss.chatapp.com.server.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {
    
    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String toEmail;

    public void SendEmail(String name, String email, String content, String subject) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String mailContent = 
        """
            <p><b>Sender Email:<b> %s</p>
            <p><b>Name:<b> %s</p>
            <p><b>Content:<b> %s</p>
        """.formatted(email, name, content);


        helper.setFrom(toEmail,"WhatsChat FAQ");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        javaMailSender.send(message);
    
        System.out.println("Mail send" + helper.toString());
    }

}
