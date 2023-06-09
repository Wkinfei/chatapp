package nus.iss.chatapp.com.server.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class UpdateFriend {
    private String type;
    private FriendProfile profile;
}
