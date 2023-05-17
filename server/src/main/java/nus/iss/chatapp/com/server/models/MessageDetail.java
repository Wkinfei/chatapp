package nus.iss.chatapp.com.server.models;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class MessageDetail {
    private String text;
    private Integer chatId;
    private Integer senderId;
    private LocalDateTime msgTime;
    private String msgType;
        
}
