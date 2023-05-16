package nus.iss.chatapp.com.server.utils;

public class Quries {
    // public static final String SQL_PROFILE_SELECT_BY_ID = """
    //     SELECT * FROM user_profiles WHERE user_id IN (
    //         SELECT user_id1 FROM user_relationship WHERE user_id2 =?
    //         UNION
    //         SELECT user_id2 FROM user_relationship WHERE user_id1 =?
    //         );
    //         """;

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

    
}
