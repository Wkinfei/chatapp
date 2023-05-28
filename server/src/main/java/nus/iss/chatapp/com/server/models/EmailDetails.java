package nus.iss.chatapp.com.server.models;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @NoArgsConstructor @ToString
public class EmailDetails {


    private String name;


    private String email;


    private String content;


    private String subject;
}
