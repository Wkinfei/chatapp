package nus.iss.chatapp.com.server.models;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class FriendProfile {
    private Integer userId;
    private String displayName;
    private String imageUrl;
    private String text;
    private LocalDateTime msgTime;
    private String msgType;

}
