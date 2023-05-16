package nus.iss.chatapp.com.server.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.repositories.MessageMongoRepository;
import nus.iss.chatapp.com.server.repositories.ProfileRepository;
import nus.iss.chatapp.com.server.services.MessageService;
import nus.iss.chatapp.com.server.services.SocketMessageService;


@RestController
@RequestMapping(path="/api/chat", produces=MediaType.APPLICATION_JSON_VALUE)
public class ChatController {

    @Autowired
    SocketMessageService socketMessageService;

    @Autowired
    MessageService messageService;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    MessageMongoRepository msgRepo;
    
    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(@RequestBody MessageDetail payload) {

        System.out.println("payload at controller"+ payload);
        messageService.insertMessage(payload);
        socketMessageService.sendMessage(payload);
   
    }

    @GetMapping(path="/{chatId}")
    public List<MessageDetail> getAllMessagesByChatId(@PathVariable Integer chatId){
        return messageService.getAllMessagesByChatId(chatId);
    }
    // @GetMapping()
    // public List<Relationship> test(){
    //     return profileRepository.getAllRelationships();
    //    }
    
    @DeleteMapping(path="/{chatId}")
    public void test(@PathVariable Integer chatId){
        msgRepo.deleteMessages(chatId);
    }
    
}
