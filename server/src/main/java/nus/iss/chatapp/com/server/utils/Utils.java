package nus.iss.chatapp.com.server.utils;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.models.TenorGif;

public class Utils {

    public static List<MessageDetail> fromMongoDocument(List<Document> docs) {
        List<MessageDetail> msgs = new LinkedList<>();
        
        for (Document document : docs) {
            MessageDetail msg = new MessageDetail();
            msg.setChatId(document.getInteger("chatId"));
            msg.setSenderId(document.getInteger("senderId"));
            msg.setText(document.getString("text"));
            msg.setMsgType(document.getString("msgType"));
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
        msg.setMsgType(document.getString("msgType"));
        msg.setMsgTime(document.getDate("msgTime").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        
        return msg;
    }

    public static Document toDocument(MessageDetail message) {
        Document doc = new Document();
        doc.append("chatId",message.getChatId())
            .append("senderId",message.getSenderId())
            .append("text", message.getText())
            .append("msgType", message.getMsgType())
            .append("msgTime",LocalDateTime.now());
        return doc;
    }

    public static JsonObject toJson(String str) {
		Reader reader = new StringReader(str);
		JsonReader jsonReader = Json.createReader(reader);
		return jsonReader.readObject();
	}

    public static TenorGif toGif(JsonObject json) {
        TenorGif gif = new TenorGif();
        gif.setTinyGifUrl(json.getJsonObject("media_formats").getJsonObject("tinygif").getString("url"));
        gif.setNanoGifUrl(json.getJsonObject("media_formats").getJsonObject("nanogif").getString("url"));
        return gif;
    }
    
    public static JsonObject toJsonObj(TenorGif gif) {
        return Json.createObjectBuilder()
                .add("tinyGifUrl",gif.getTinyGifUrl())
                .add("nanoGifUrl",gif.getNanoGifUrl())
                .build();
    }
}
