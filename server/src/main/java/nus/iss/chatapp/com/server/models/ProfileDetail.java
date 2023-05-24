package nus.iss.chatapp.com.server.models;

import java.util.UUID;
import nus.iss.chatapp.com.server.utils.Utils;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class ProfileDetail {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String imageUrl;
    private String role = "ROLE_USER";
    private Boolean enabled = true;

    public ProfileDetail(String username, String password, String email) {
        this.username = username;
        // TODO: hashPassword 
        this.password = Utils.hashPassword(password);
        this.email = email;
        this.imageUrl = "https://robohash.org/%s.png?set=set4&size=100x100"
                            .formatted(UUID.randomUUID().toString().substring(0, 16));
    }

   
}
