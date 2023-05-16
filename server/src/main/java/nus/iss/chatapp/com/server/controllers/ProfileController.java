package nus.iss.chatapp.com.server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import nus.iss.chatapp.com.server.models.FriendProfile;
import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.models.Relationship;
import nus.iss.chatapp.com.server.services.ProfileService;
import nus.iss.chatapp.com.server.services.SocketProfileService;

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

  //  @DeleteMapping(path="/{userId}/{friendId}")
    //  public void deleteFriend(@PathVarriable Integer userId, @PathVarriable Integer friendId) {

  //  @DeleteMapping(path="/{friendId}")
  // public void deleteFriend(@RequestBody ProfileDetail payload, @PathVarriable Integer friendId) {

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
}
