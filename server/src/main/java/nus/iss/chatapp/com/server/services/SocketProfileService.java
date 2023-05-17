package nus.iss.chatapp.com.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.chatapp.com.server.models.FriendProfile;
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

    public void updateDelete(Relationship rs) {
        
        FriendProfile profile1 = new FriendProfile();
        profile1.setUserId(rs.getUserId1());

        UpdateFriend update1 = new UpdateFriend();
        update1.setType("delete");
        update1.setProfile(profile1);

        socketService.sendProfile(rs.getUserId2(), update1);

        //-----------------------------------------

        FriendProfile profile2 = new FriendProfile();
        profile2.setUserId(rs.getUserId2());

        UpdateFriend update2 = new UpdateFriend();
        update2.setType("delete");
        update2.setProfile(profile2);

        socketService.sendProfile(rs.getUserId1(), update2);

    }

    //add friend
    
     public void updateAdd(Integer id1,Integer id2){

    FriendProfile profile1 = new FriendProfile();
   
    ProfileDetail profileDetail1 = profileRepo.getUserProfileByUserId(id1);
    profile1.setUserId(id1);
    profile1.setDisplayName(profileDetail1.getDisplayName());
    profile1.setImageUrl(profileDetail1.getImageUrl());

        UpdateFriend update1 = new UpdateFriend();
        update1.setType("add");
        update1.setProfile(profile1);

        socketService.sendProfile(id2, update1);
    //-----------------------------------------
    FriendProfile profile2 = new FriendProfile();
    ProfileDetail profileDetail2 = profileRepo.getUserProfileByUserId(id2);
    profile2.setUserId(id2);
    profile2.setDisplayName(profileDetail2.getDisplayName());
    profile2.setImageUrl(profileDetail2.getImageUrl());
        
        UpdateFriend update2 = new UpdateFriend();
        update2.setType("add");
        update2.setProfile(profile2);

        socketService.sendProfile(id1, update2);
     }
     
    

}
