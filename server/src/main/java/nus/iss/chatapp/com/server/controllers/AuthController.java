package nus.iss.chatapp.com.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import nus.iss.chatapp.com.server.models.LoginDetails;
import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.services.ProfileService;
import nus.iss.chatapp.com.server.utils.Utils;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    ProfileService profileService;

    @PostMapping(path="/sign-up")
    public ResponseEntity<Void> addNewUser(@RequestBody String payload){

        // System.out.println("SIGN UP>>>>>>>"+ payload);
        JsonObject json = Utils.toJson(payload);

        ProfileDetail detail = new ProfileDetail(
                                        json.getString("username"), 
                                        json.getString("password"), 
                                        json.getString("email"));
        profileService.addNewUser(detail);

        return ResponseEntity
        .status(HttpStatus.OK)
        .build();
    }

    @PostMapping(path="/log-in")
    public String login(@RequestBody String payload) {
        // System.out.println("LOG IN"+ payload);
        JsonObject json = Utils.toJson(payload);
        LoginDetails login = new LoginDetails(json.getString("email"), 
                                                    json.getString("password"));

        String jwt = profileService.authenticateUser(login.getEmail(), login.getPassword());

        // System.out.println(jwt);

        return Json.createObjectBuilder()
                    .add("token", jwt)
                    .build().toString();
    }

    @GetMapping(path="/auth")
    public Boolean emailExists(@RequestParam String email) {
        return this.profileService.checkUserProfileExistsByEmail(email);
    }

    
}
