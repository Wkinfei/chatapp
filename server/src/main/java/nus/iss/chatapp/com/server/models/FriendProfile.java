package nus.iss.chatapp.com.server.models;

import java.time.LocalDateTime;

public class FriendProfile {
    private Integer userId;
    private String displayName;
    private String imageUrl;
    private String text;
    private LocalDateTime msgTime;

    
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    public LocalDateTime getMsgTime() {
        return msgTime;
    }
    public void setMsgTime(LocalDateTime msgTime) {
        this.msgTime = msgTime;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return "FriendProfile [userId=" + userId + ", displayName=" + displayName + ", imageUrl=" + imageUrl + ", text="
                + text + ", msgTime=" + msgTime + "]";
    }

    
    
}
