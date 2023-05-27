package nus.iss.chatapp.com.server.services;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.chatapp.com.server.models.FriendProfile;
import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.models.Relationship;
import nus.iss.chatapp.com.server.models.UpdateFriend;
import nus.iss.chatapp.com.server.repositories.ProfileRepository;

@Service
public class SocketProfileService {
    
    @Autowired
    SocketService socketService;

    @Autowired
    ProfileRepository profileRepo;

    @Autowired
    ProfileService profileService;

    //Delete friend
    public void updateDelete(Relationship rs) {

        // this.updateFriend("delete", rs.getUserId1(), rs.getUserId2());

        // FriendProfile profile1 = new FriendProfile();
        // profile1.setUserId(rs.getUserId1());

        // UpdateFriend update1 = new UpdateFriend();
        // update1.setType("delete");
        // update1.setProfile(profile1);

        ProfileDetail profileDetail1 = profileRepo.getUserProfileByUserId(rs.getUserId1());
        UpdateFriend update1 = this.toUpdateFriend("delete", profileDetail1);

        ProfileDetail profileDetail2 = profileRepo.getUserProfileByUserId(rs.getUserId2());
        UpdateFriend update2 = this.toUpdateFriend("delete", profileDetail2);

        socketService.sendProfile(rs.getUserId2(), update1);
        socketService.sendProfile(rs.getUserId1(), update2);

    }

    //Add friend
    public void updateAdd(Integer id1,Integer id2){

        // FriendProfile profile1 = new FriendProfile();
        // profile1.setUserId(id1);
        // profile1.setDisplayName(profileDetail1.getUsername());
        // profile1.setImageUrl(profileDetail1.getImageUrl());
        // UpdateFriend update1 = new UpdateFriend();
        // update1.setType("add");
        // update1.setProfile(profile1);

        ProfileDetail profileDetail1 = profileRepo.getUserProfileByUserId(id1);
        UpdateFriend update1 = this.toUpdateFriend("add", profileDetail1);

        ProfileDetail profileDetail2 = profileRepo.getUserProfileByUserId(id2);
        UpdateFriend update2 = this.toUpdateFriend("add", profileDetail2);

        socketService.sendProfile(id2, update1);
        socketService.sendProfile(id1, update2);
     }
     
    //New Message
    public void updateNewMsg(MessageDetail msg){
        List<Relationship> relationships = profileRepo.getRelationships(msg.getSenderId());
    
        Relationship rs = relationships.stream().filter(
                                r -> r.getChatId() == msg.getChatId() )
                                .findFirst().get();

        Integer receiverId = (msg.getSenderId() == rs.getUserId1()) ? 
                                rs.getUserId2() : rs.getUserId1();

        Optional<FriendProfile> optReceiverProfile = profileService.getFriendProfile(msg.getSenderId(), receiverId);
        if(optReceiverProfile.isEmpty()){
            // TODO: Throw error
            // Should never happen
           
        }

        UpdateFriend updateReceiver = toUpdateFriend("message", optReceiverProfile.get());
        socketService.sendProfile(msg.getSenderId(), updateReceiver);

        
        Optional<FriendProfile> optSenderProfile = profileService.getFriendProfile(receiverId, msg.getSenderId());
        if(optSenderProfile.isEmpty()){
            // TODO: Throw error
            // Should never happen
           
        }
        UpdateFriend updateSender = toUpdateFriend("message", optSenderProfile.get());
        socketService.sendProfile(receiverId, updateSender);
    }

    //Update user name
    public void updateUserName(Integer userId){
        //find all ids related to the login user
        List<Relationship> relationships = profileRepo.getRelationships(userId);
        //filter relationships by userId to get list of ReceiverId
        List<Integer> receiverIds = new LinkedList<>();
        for(Relationship rs : relationships){
                Integer receiverId = (userId == rs.getUserId1())? rs.getUserId2() : rs.getUserId1();
                receiverIds.add(receiverId);
        }
      
        for(Integer receiverId : receiverIds){
            Optional<FriendProfile> optSenderProfile = profileService.getFriendProfile(receiverId,userId);
            if(optSenderProfile.isEmpty()){
                // TODO: Throw error
              
            }
            UpdateFriend updateSender = toUpdateFriend("edit", optSenderProfile.get());
            socketService.sendProfile(receiverId, updateSender);
        }

    }

    private UpdateFriend toUpdateFriend(String type, FriendProfile profile) {

        UpdateFriend update = new UpdateFriend();
        update.setType(type);
        update.setProfile(profile);

        return update;
    }

    private UpdateFriend toUpdateFriend(String type, ProfileDetail profileDetail) {
        return this.toUpdateFriend(type, profileDetail, null);
    }

    private UpdateFriend toUpdateFriend(String type, ProfileDetail profileDetail, MessageDetail msg) {
        FriendProfile profile = new FriendProfile();
        profile.setUserId(profileDetail.getUserId());
        profile.setUsername(profileDetail.getUsername());
        profile.setImageUrl(profileDetail.getImageUrl());
        if(null == msg) {
            profile.setMsgType("text");
            profile.setText("No Message");
          
        } else {
            profile.setMsgType(msg.getMsgType());
            profile.setText(msg.getText());
            profile.setMsgTime(msg.getMsgTime());
        }

        UpdateFriend update = new UpdateFriend();
        update.setType(type);
        update.setProfile(profile);

        return update;
    }


}
