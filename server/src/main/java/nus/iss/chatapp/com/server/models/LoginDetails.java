package nus.iss.chatapp.com.server.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.ToString;

@Getter @NoArgsConstructor @ToString @AllArgsConstructor
public class LoginDetails {
    String email;
    String password;
}
