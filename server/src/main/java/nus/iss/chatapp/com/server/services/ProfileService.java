package nus.iss.chatapp.com.server.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtEncoder;



@Service
public class ProfileService {
    
    @Autowired
    ProfileRepository profileRepo;

    @Autowired 
    MessageMongoRepository messageMongoRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtEncoder jwtEncoder;

    public List<FriendProfile> getFriendProfiles(Integer userId) {

        List<Relationship> relationships = profileRepo.getRelationships(userId);
        List<ProfileDetail> profiles = profileRepo.getProfileDetails();
        List<Document> docs = messageMongoRepository.getFriendListMessages();
        // List<MessageDetail> messages = Utils.fromMongoDocument(docs);

        List<MessageDetail> messages = docs
                                    .stream()
                                    .map(d -> Utils.toMessageDetail(d))
                                    .toList();

        // profiles <--> relationships <--> messages  ==> FriendProfile

        List<FriendProfile> friendProfiles = new LinkedList<>();

        for (Relationship rs : relationships) {
            FriendProfile friendProfile = new FriendProfile();
            // Set FROM profiles
                Integer id = (userId == rs.getUserId1()) ?  rs.getUserId2(): rs.getUserId1();
                // ProfileDetail profileDetail = profiles.find( x => x.id === id)
            
                ProfileDetail profileDetail = profiles.stream().filter(profile -> profile.getUserId() == id).findFirst().get();
                friendProfile.setUsername(profileDetail.getUsername());
                friendProfile.setImageUrl(profileDetail.getImageUrl());
                friendProfile.setUserId(profileDetail.getUserId());
            // Set FROM messages
                // Message message = messages.find(x => x.chatId === rs.chatId)
                Optional<MessageDetail> opt = messages.stream().filter(msg -> msg.getChatId() == rs.getChatId()).findFirst();

                if(opt.isEmpty()) {

                    friendProfile.setText("no message");
                    // friendProfile.setMsgTime(LocalDateTime.now());

                } else {
                    MessageDetail messageDetail = opt.get();
                    friendProfile.setText(messageDetail.getText());
                    friendProfile.setMsgTime(messageDetail.getMsgTime());
                    friendProfile.setMsgType(messageDetail.getMsgType());

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

    public ProfileDetail getProfileDetailById(Integer id){
        return profileRepo.getUserProfileByUserId(id);
    }

    public Integer addRelationship(Integer id1, String email){
        ProfileDetail user = profileRepo.getUserProfileByEmail(email);
        Integer userId1 = Math.min(id1,user.getUserId());
        Integer userId2 = Math.max(id1,user.getUserId());

        //if validate = 1 {insert sql}
        if(profileRepo.checkRelationshipExist(userId1, userId2) == false){
            return profileRepo.addRelationship(userId1, userId2);
        }else{
            return 0;
        }
    }

    public Optional<FriendProfile> getFriendProfile(Integer userId, Integer friendId){

        List<FriendProfile> friendProfiles = getFriendProfiles(userId);
        Optional<FriendProfile> optFriendProfiles = friendProfiles.stream()
                                                                .filter(profile -> profile.getUserId() == friendId)
                                                                .findFirst();
        return optFriendProfiles;
    }

    public Integer updateUserNameByID(String updateName, Integer id){
        return profileRepo.updateUserNameByID(updateName, id);
    }

    public Integer addNewUser(ProfileDetail detail){

        if (profileRepo.checkUserProfileExistsByEmail(detail.getEmail()) == false) {

            return profileRepo.insertNewUserProfile(detail);

        } else {
            return 0;
        }
    }

    public String authenticateUser(String email, String password) {

        UsernamePasswordAuthenticationToken authRequest = 
            UsernamePasswordAuthenticationToken.unauthenticated(email,password);

        Authentication authentication = authenticationManager.authenticate(authRequest);
        
        if(!authentication.isAuthenticated()) {
            // not authenticated    
        } 

        ProfileDetail profileDetail = profileRepo.getUserProfileByEmail(email);
        
        return Utils.generateJwtToken(authentication, profileDetail, jwtEncoder);
        
    }
}
