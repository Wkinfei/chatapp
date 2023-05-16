package nus.iss.chatapp.com.server.models;

import java.time.LocalDateTime;

public class MessageDetail {
    private String text;
    private Integer chatId;
    private Integer senderId;
    private LocalDateTime msgTime;
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Integer getChatId() {
        return chatId;
    }
    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
    public Integer getSenderId() {
        return senderId;
    }
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
    public LocalDateTime getMsgTime() {
        return msgTime;
    }
    public void setMsgTime(LocalDateTime msgTime) {
        this.msgTime = msgTime;
    }
    @Override
    public String toString() {
        return "MessageDetail [text=" + text + ", chatId=" + chatId + ", senderId=" + senderId + ", msgTime=" + msgTime
                + "]";
    }

    
   

        
}
