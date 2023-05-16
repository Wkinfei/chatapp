package nus.iss.chatapp.com.server.models;

public class Relationship {
    private Integer chatId;
    private Integer userId1;
    private Integer userId2;
    
    public Integer getChatId() {
        return chatId;
    }
    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
    public Integer getUserId1() {
        return userId1;
    }
    public void setUserId1(Integer userId1) {
        this.userId1 = userId1;
    }
    public Integer getUserId2() {
        return userId2;
    }
    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }
    @Override
    public String toString() {
        return "Relationship [chatId=" + chatId + ", userId1=" + userId1 + ", userId2=" + userId2 + "]";
    }

    
}
