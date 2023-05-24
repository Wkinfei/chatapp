package nus.iss.chatapp.com.server.utils;

import java.io.Reader;
import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

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

    public static String hashPassword (String password) {
 
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
             
        return hash;
        }
    
    public static String generateJwtToken (Authentication authentication, ProfileDetail profileDetail, JwtEncoder encoder) {

            String scope = authentication.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .collect(Collectors.joining(" "));
    
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(60*60*12))
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .claim("email", profileDetail.getEmail())
                    .claim("username", profileDetail.getUsername())
                    .claim("userId", profileDetail.getUserId())
                    .build();
    
            return encoder.encode(JwtEncoderParameters.from(claims))
                            .getTokenValue();
        }
}
