package nus.iss.chatapp.com.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.repositories.MessageMongoRepository;

@Service
public class MessageService {
    @Autowired
    MessageMongoRepository messageMongoRepository;

    public void insertMessage(MessageDetail message){
        messageMongoRepository.insertMessage(message);
    }

    public List<MessageDetail> getAllMessagesByChatId(Integer chatId){
        return messageMongoRepository.getMessagesByChatId(chatId);
    }
}
