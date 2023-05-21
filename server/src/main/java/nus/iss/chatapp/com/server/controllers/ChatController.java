package nus.iss.chatapp.com.server.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.TenorGif;
import nus.iss.chatapp.com.server.repositories.MessageMongoRepository;
import nus.iss.chatapp.com.server.repositories.ProfileRepository;
import nus.iss.chatapp.com.server.services.GifService;
import nus.iss.chatapp.com.server.services.MessageService;
import nus.iss.chatapp.com.server.services.SocketMessageService;
import nus.iss.chatapp.com.server.services.SocketProfileService;
import nus.iss.chatapp.com.server.utils.Utils;


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

    @Autowired
    GifService gifService;

    @Autowired
    SocketProfileService socketProfileService;
    
    @PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
    public void sendMessage(@RequestBody MessageDetail payload) {
        // System.out.println("payload at controller"+ payload);
        messageService.insertMessage(payload);
        socketMessageService.sendMessage(payload);
        //Add socketProfile
        socketProfileService.updateNewMsg(payload);
    }

    @GetMapping(path="/{chatId}")
    public List<MessageDetail> getAllMessagesByChatId(@PathVariable Integer chatId){
        return messageService.getAllMessagesByChatId(chatId);
    }
 
    @DeleteMapping(path="/{chatId}")
    public void test(@PathVariable Integer chatId){
        msgRepo.deleteMessages(chatId);
    }
    
    @GetMapping("/gif")
    public ResponseEntity<String> test(@RequestParam String q,@RequestParam(defaultValue = "8")Integer limit) {
       
        System.out.println("Tenor Gif search term>>" + q);
        List<TenorGif> gifs = gifService.getGif(q, limit);
       
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (TenorGif g : gifs) {
            arrBuilder.add(Utils.toJsonObj(g));
        }
		  
		return ResponseEntity.ok(Json.createObjectBuilder()
								.add("gifs", arrBuilder)
								.build().toString());
    }
}
