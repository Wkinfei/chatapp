package nus.iss.chatapp.com.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import ch.qos.logback.classic.pattern.Util;
import nus.iss.chatapp.com.server.models.ProfileDetail;
import nus.iss.chatapp.com.server.models.Relationship;

import static nus.iss.chatapp.com.server.utils.Quries.*;

import java.util.LinkedList;
import java.util.List;

@Repository
public class ProfileRepository {

    private final String SQL_GET_RELATIONSHIP_BY_IDS = 
    """
    SELECT * FROM user_relationship WHERE user_id1= LEAST(?, ?) and user_id2=GREATEST(?, ?) LIMIT 1;    
    """;

    
    
    @Autowired
    JdbcTemplate template;

    // public List<ProfileDetail> findProfilesById(Integer id1) {
    //     // List<ProfileDetail> profileDetails = new LinkedList<>();

    //     // SqlRowSet rs = template.queryForRowSet(SQL_GET_FRIEND_PROFILE_DETAILS );

    //     // while(rs.next()){
    //     //     profileDetails.add(Utils.fromSQL(rs));
    //     // }

    //     //new BeanPropertyRowMapper<>(ProfileDetail.class) will map the sql object to model object
    //     return template.query(SQL_PROFILE_SELECT_BY_ID, 
    //                         new BeanPropertyRowMapper<>(ProfileDetail.class), 
    //                         id1, id1);

    // }

    // public List<Integer> findChatIdsById(Integer id){
    //     return template.queryForList(SQL_RELATIONSHIP_FIND_CHAT_IDS, Integer.class, id, id);
    // //    return template.query(SQL_PROFILE_SELECT_BY_ID, new BeanPropertyRowMapper<>(Integer.class), id,id);
    // }


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
        return template.queryForObject(SQL_GET_RELATIONSHIP_BY_IDS, new BeanPropertyRowMapper<>(Relationship.class), id1, id2, id1, id2);
    }
}
