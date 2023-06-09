package nus.iss.chatapp.com.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.models.Relationship;

import static nus.iss.chatapp.com.server.utils.Quries.*;

import java.util.List;

@Repository
public class ProfileRepository {

    @Autowired
    JdbcTemplate template;

    public List<Relationship> getRelationships(Integer id){
        return template.query(SQL_RELATIONSHIP_GET_RELATIONSHIPS_BY_ID, new BeanPropertyRowMapper<>(Relationship.class), id, id);
    }

    public List<ProfileDetail> getProfileDetails(){
        return template.query(SQL_PROFILE_GET_PROFILES, new BeanPropertyRowMapper<>(ProfileDetail.class));
    }

    public List<Relationship> getAllRelationships(){
        return template.query(SQL_RELATIONSHIP_GET_ALL_RELATIONSHIP, new BeanPropertyRowMapper<>(Relationship.class));
    }

    public void deleteRelationship(Integer chatId){
        Integer deleted = template.update(SQL_RELATIONSHIP_DELETE_BY_CHATID, chatId);
        System.out.println("DeletedById --> " + deleted);
        
    }

    public Relationship getRelationshipByIds(Integer id1, Integer id2) {
        return template.queryForObject(SQL_GET_RELATIONSHIP_BY_IDS, 
                                        new BeanPropertyRowMapper<>(Relationship.class),
                                         id1, id2, id1, id2);
    }

    public Integer addRelationship(Integer id1, Integer id2){
        return template.update(SQL_RELATIONSHIP_ADD_FRIEND, id1,id2);
    }

    public Boolean checkRelationshipExist(Integer id1, Integer id2){
        Boolean isExists = template.queryForObject(
                                        SQL_RELATIONSHIP_CHECK_IDS_IS_EXISTS,
                                         Boolean.class, id1,id2);
        return isExists;   
    }

    public ProfileDetail getUserProfileByEmail(String email){
        return template.queryForObject(SQL_PROFILE_GET_BY_EMAIL, new BeanPropertyRowMapper<>(ProfileDetail.class), email);
    }

    public ProfileDetail getUserProfileByUserId(Integer id){
        return template.queryForObject(SQL_PROFILE_GET_BY_ID, new BeanPropertyRowMapper<>(ProfileDetail.class), id);
    }

    public Relationship getRelationshipByIDs(Integer id1, Integer id2){
        return template.queryForObject(SQL_GET_RELATIONSHIP_BY_IDS, Relationship.class, id1,id2);
    }

    public Integer updateUserNameByID(String updateName, Integer id){
        return template.update(SQL_PROFILE_UPDATE_USERNAME_BY_ID, updateName,id);
    }

    public Boolean checkUserProfileExistsByEmail(String email){
        Boolean isExists = template.queryForObject(
                                        SQL_PROFILE_CHECK_EMAIL_IS_EXISTS,
                                         Boolean.class, email);
        return isExists;   
    }

    public Integer insertNewUserProfile(ProfileDetail detail){

       Integer inserted =  template.update(SQL_PROFILE_INSERT_NEW_USER, 
                                        detail.getUsername(), detail.getPassword(), 
                                        detail.getEmail(), detail.getImageUrl(), 
                                        detail.getRole(), detail.getEnabled());
        return inserted;
    }
}
