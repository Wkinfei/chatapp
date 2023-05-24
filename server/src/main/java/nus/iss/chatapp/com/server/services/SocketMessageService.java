package nus.iss.chatapp.com.server.services;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.Relationship;
import nus.iss.chatapp.com.server.repositories.ProfileRepository;



@Service
public class SocketMessageService {
    @Autowired
    SocketService socketService;

    @Autowired
    ProfileRepository profileRepository;

    public void sendMessage(MessageDetail msg) {
        // System.out.println(">>" + msg);
        //use chatId and senderId to retrieve recieverId
        List<Relationship> relationships = profileRepository.getRelationships(msg.getSenderId());
    
        Relationship rs = relationships.stream().filter(
                                r -> r.getChatId() == msg.getChatId() )
                                .findFirst().get();

        Integer receiverId = (msg.getSenderId() == rs.getUserId1()) ? 
                                rs.getUserId2() : rs.getUserId1();
        
    //    System.out.println("Message Socket>>>>"+ receiverId);
        socketService.sendMessage(msg.getSenderId(), msg);
        socketService.sendMessage(receiverId, msg);
        return;
    }

    
}