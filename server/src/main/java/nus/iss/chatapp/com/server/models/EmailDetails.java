package nus.iss.chatapp.com.server.models;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @NoArgsConstructor @ToString
public class EmailDetails {

    @NotBlank(message="name cannot be blank or null")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message="Please fill up this field.")
    private String content;

    @NotBlank(message="Please fill up this field.")
    private String subject;
}
