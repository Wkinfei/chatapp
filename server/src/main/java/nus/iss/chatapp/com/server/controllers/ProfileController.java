package nus.iss.chatapp.com.server.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import nus.iss.chatapp.com.server.models.FriendProfile;
import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.models.Relationship;
import nus.iss.chatapp.com.server.services.ProfileService;
import nus.iss.chatapp.com.server.services.SocketProfileService;
import nus.iss.chatapp.com.server.utils.Utils;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @Autowired
    SocketProfileService socketProfileService;
    
  @GetMapping(path="/{id}")
  public List<FriendProfile> getFriendProfiles(@PathVariable Integer id){
    return profileService.getFriendProfiles(id);
   }

   @GetMapping(path="/relationship")
   public List<Relationship> getRelationships(){
    return profileService.getAllRelationships();
   }

   @DeleteMapping()
   public ResponseEntity<Void> deleteRelationship(@RequestBody Relationship rs) {

    Integer id1 = rs.getUserId1();
    Integer id2 = rs.getUserId2();

    profileService.deleteFriendData(id1, id2);
    socketProfileService.updateDelete(rs);

    return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
   }

   @PostMapping("/{id}/addfriend")
    public ResponseEntity<String> addFriend(@RequestBody String json,@PathVariable Integer id){
      
      JsonObject j = Utils.toJson(json);
      String email = j.getString("email");
    
      profileService.addRelationship(id, email);

          //socket
          ProfileDetail profile = profileService.getProfileDetail(email);
          socketProfileService.updateAdd(id, profile.getUserId());
          return ResponseEntity
                  .status(HttpStatus.ACCEPTED)
                  .build();
        
        }

      @PutMapping("/{id}")
      public ResponseEntity<String> updateUserName(@RequestBody String json,@PathVariable Integer id){
        JsonObject j = Utils.toJson(json);
        String userName = j.getString("username");
        Integer counts = profileService.updateUserNameByID(userName, id);
        // System.out.println("update>>>"+ counts);
        if(counts == 0){
          return ResponseEntity
                  .status(HttpStatus.BAD_REQUEST)
                  .contentType(MediaType.APPLICATION_JSON)
                    .body(Json.createObjectBuilder()
                             .add("message"," ")
                             .build().toString());
        }
         //socket
         socketProfileService.updateUserName(id);
         return ResponseEntity
                 .status(HttpStatus.ACCEPTED)
                 .build();
      }

      @GetMapping("/userprofile/{id}")
      public ResponseEntity<String> getProfile(@PathVariable Integer id){
       // return only imageUrl & username
       ProfileDetail userProfile = profileService.getProfileDetailById(id);
        JsonObject j = Utils.toJsonObj(userProfile);
        return ResponseEntity.ok()
                              .body(j.toString());                  
      }

}
