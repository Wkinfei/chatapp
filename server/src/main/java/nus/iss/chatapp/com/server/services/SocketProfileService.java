package nus.iss.chatapp.com.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nus.iss.chatapp.com.server.models.FriendProfile;
import nus.iss.chatapp.com.server.models.Relationship;
import nus.iss.chatapp.com.server.models.UpdateFriend;

@Service
public class SocketProfileService {
    
    @Autowired
    SocketService socketService;

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

    

}
