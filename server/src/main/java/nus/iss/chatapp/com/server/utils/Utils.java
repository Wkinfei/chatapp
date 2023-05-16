package nus.iss.chatapp.com.server.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.ProfileDetail;

public class Utils {

    public static List<MessageDetail> fromMongoDocument(List<Document> docs) {
        List<MessageDetail> msgs = new LinkedList<>();
        
        for (Document document : docs) {
            MessageDetail msg = new MessageDetail();
            msg.setChatId(document.getInteger("chatId"));
            msg.setSenderId(document.getInteger("senderId"));
            msg.setText(document.getString("text"));
            msg.setMsgTime(document.getDate("msgTime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            msgs.add(msg);
        }
        // LocalDateTime.parse(rs.getString("last_msg_time")
        return msgs;
    }

    public static MessageDetail toMessageDetail(Document document) {
        
        MessageDetail msg = new MessageDetail();
        msg.setChatId(document.getInteger("chatId"));
        msg.setSenderId(document.getInteger("senderId"));
        msg.setText(document.getString("text"));
        msg.setMsgTime(document.getDate("msgTime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        
        return msg;
    }

    public static Document toDocument(MessageDetail message) {
        Document doc = new Document();
        doc.append("chatId",message.getChatId())
            .append("senderId",message.getSenderId())
            .append("text", message.getText())
            .append("msgTime",LocalDateTime.now());
        return doc;
    }

}
