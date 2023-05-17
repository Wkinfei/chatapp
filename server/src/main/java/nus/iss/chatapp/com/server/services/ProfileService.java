package nus.iss.chatapp.com.server.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nus.iss.chatapp.com.server.exceptions.DeleteUserDataException;
import nus.iss.chatapp.com.server.models.FriendProfile;
import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.models.Relationship;
import nus.iss.chatapp.com.server.repositories.MessageMongoRepository;
import nus.iss.chatapp.com.server.repositories.ProfileRepository;
import nus.iss.chatapp.com.server.utils.Utils;

@Service
public class ProfileService {
    
    @Autowired
    ProfileRepository profileRepo;

    @Autowired 
    MessageMongoRepository messageMongoRepository;

    public List<FriendProfile> getFriendProfiles(Integer id) {

        List<Relationship> relationships = profileRepo.getRelationships(id);
        List<ProfileDetail> profiles = profileRepo.getProfileDetails();
        List<Document> docs = messageMongoRepository.getFriendListMessages();
        // List<MessageDetail> messages = Utils.fromMongoDocument(docs);

        List<MessageDetail> messages = docs
                                    .stream()
                                    .map(d -> Utils.toMessageDetail(d))
                                    .toList();

        System.out.println("rs >>" + relationships);

        return FriendProfiles(profiles, relationships, messages, id);
    }


    private List<FriendProfile> FriendProfiles(List<ProfileDetail> profiles, 
                    List<Relationship> relationships, 
                    List<MessageDetail> messages, Integer myId) {

        // profiles <--> relationships <--> messages  ==> FriendProfile

        List<FriendProfile> friendProfiles = new LinkedList<>();

        for (Relationship rs : relationships) {
            FriendProfile friendProfile = new FriendProfile();
            // Set FROM profiles
                Integer id = (myId == rs.getUserId1()) ?  rs.getUserId2(): rs.getUserId1();
                // ProfileDetail profileDetail = profiles.find( x => x.id === id)
                ProfileDetail profileDetail = profiles.stream().filter(profile -> profile.getuserId() == id).findFirst().get();
                friendProfile.setDisplayName(profileDetail.getDisplayName());
                friendProfile.setImageUrl(profileDetail.getImageUrl());
                friendProfile.setUserId(profileDetail.getuserId());
            // Set FROM messages
                // Message message = messages.find(x => x.chatId === rs.chatId)
                Optional<MessageDetail> opt = messages.stream().filter(msg -> msg.getChatId() == rs.getChatId()).findFirst();

                if(opt.isEmpty()) {
                    // if not exist then set nulls
                    friendProfile.setText("no message");
                    // friendProfile.setMsgTime();
                    // OR set default in model

                } else {
                    MessageDetail messageDetail = opt.get();
                    friendProfile.setText(messageDetail.getText());
                    friendProfile.setMsgTime(messageDetail.getMsgTime());

                }
                friendProfiles.add(friendProfile);                
        }
        return friendProfiles;
    }

    public List<Relationship> getAllRelationships(){
        return profileRepo.getAllRelationships();
    }

    @Transactional(rollbackFor = DeleteUserDataException.class)
    public void deleteFriendData(Integer userId, Integer friendId){

        // get chat_id from relationsip table, using userId and friendId
        Relationship rs = profileRepo.getRelationshipByIds(userId, friendId);
        Integer chatId = rs.getChatId();

        // delete relationship from relationship table, using chat_id
        try {
            profileRepo.deleteRelationship(chatId);
        } catch (Exception e) {
            // rollback if fail
            throw new DeleteUserDataException(e.getMessage());
        }

        // delete chat from messageMongo collection, using chat_id
        try {
            messageMongoRepository.deleteMessages(chatId);
        } catch (Exception e) {
            // rollback if fail
            throw new DeleteUserDataException(e.getMessage());
        }
    }

    public ProfileDetail getProfileDetail(String email){
        return profileRepo.getUserProfileByEmail(email);
    }

    public Integer addRelationship(Integer id1, String email){
        ProfileDetail user = profileRepo.getUserProfileByEmail(email);
        Integer userId1 = Math.min(id1,user.getuserId());
        Integer userId2 = Math.max(id1,user.getuserId());

        //if validate = 1 {insert sql}
        if(profileRepo.checkRelationshipExist(userId1, userId2) == false){
            return profileRepo.addRelationship(userId1, userId2);
        }else{
            return 0;
        }
    }

    public FriendProfile getFriendProfile(Integer id1, Integer id2){

        //get chat id to get msg
        //id to get user profile
        return null;
    }
}
