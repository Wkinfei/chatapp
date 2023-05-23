package nus.iss.chatapp.com.server.models;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @NoArgsConstructor @ToString
public class Email {
    private String name;
    private String email;
    private String content;
    private String subject;
}
