package nus.iss.chatapp.com.server.utils;

public class Quries {

    public static final String SQL_RELATIONSHIP_GET_ALL_RELATIONSHIP = """
        SELECT * FROM user_relationship;
            """;

    public static final String SQL_RELATIONSHIP_GET_RELATIONSHIPS_BY_ID = """
        SELECT * FROM user_relationship WHERE user_id1 = ? or user_id2 = ?;
            """;

    public static final String SQL_PROFILE_GET_PROFILES = """
        SELECT * FROM user_profiles;
            """;

    public static final String SQL_RELATIONSHIP_DELETE_BY_CHATID = """
        DELETE FROM user_relationship WHERE chat_id = ?;
            """;

    public static final String SQL_GET_RELATIONSHIP_BY_IDS = 
    """
    SELECT * FROM user_relationship WHERE user_id1= LEAST(?, ?) and user_id2=GREATEST(?, ?) LIMIT 1;    
    """;

    public static final String SQL_RELATIONSHIP_ADD_FRIEND = """
        INSERT INTO user_relationship (user_id1,user_id2) VALUES(?,?);
            """;
    
    public static final String SQL_RELATIONSHIP_CHECK_IDS_IS_EXISTS = """
        SELECT EXISTS(SELECT * from user_relationship WHERE user_id1 = ? and user_id2 =?) AS `isExist`;
            """;

    public static final String SQL_PROFILE_GET_BY_EMAIL = """
        SELECT * FROM user_profiles WHERE email = ?;
            """;

    public static final String SQL_PROFILE_GET_BY_ID = """
        SELECT * FROM user_profiles WHERE user_id = ?;
            """;
}
