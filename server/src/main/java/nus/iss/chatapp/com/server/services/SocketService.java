package nus.iss.chatapp.com.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import nus.iss.chatapp.com.server.models.FriendProfile;
import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.UpdateFriend;
import nus.iss.chatapp.com.server.models.UpdateMessage;

@Service
public class SocketService {

    private final String NOTIFICATIONS_MESSAGES = "/notifications/messages/%d";
    private final String NOTIFICATIONS_PROFILES = "/notifications/profiles/%d";

    
    @Autowired
    private SimpMessagingTemplate template;

    public void sendMessage(Integer recvId, UpdateMessage update) {
        template.convertAndSend(NOTIFICATIONS_MESSAGES.formatted(recvId), update);
    }

    public void sendProfile(Integer recvId, UpdateFriend update) {
        template.convertAndSend(NOTIFICATIONS_PROFILES.formatted(recvId), update);
    }

}
