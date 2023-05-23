package nus.iss.chatapp.com.server.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import nus.iss.chatapp.com.server.models.Email;
import nus.iss.chatapp.com.server.services.EmailSenderService;

@RestController
@RequestMapping("/api/email")
public class SendEmailController {
    @Autowired
    EmailSenderService emailSenderService;
    
    @PostMapping()
      public ResponseEntity<Void> sendEmail(@RequestBody Email email) throws UnsupportedEncodingException, MessagingException{
        
        emailSenderService.SendEmail(email.getName(),email.getEmail(),
                                    email.getContent(),email.getSubject());

        return ResponseEntity
        .status(HttpStatus.OK)
        .build();

      }
}
