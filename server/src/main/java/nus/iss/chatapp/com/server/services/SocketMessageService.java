package nus.iss.chatapp.com.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.UpdateMessage;


@Service
public class SocketMessageService {
    @Autowired
    SocketService socketService;

    @Autowired
    private SimpMessagingTemplate template;


    public void sendMessage(MessageDetail msg) {
        // UpdateMessage updateMessage = new UpdateMessage();
        // updateMessage.setType("message");
        // updateMessage.setMessageDetail(msg);
        // socketService.sendMessage(msg.getSenderId(), updateMessage);
        // System.out.println(">>" + msg);
        template.convertAndSend("/notifications/messages/%d".formatted(msg.getSenderId()), msg);
        // messageMongoRepository.insertMessage(msg);
        // System.out.println("sent");
        return;
    }

    
}