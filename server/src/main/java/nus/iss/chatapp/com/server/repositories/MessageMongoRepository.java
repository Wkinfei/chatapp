package nus.iss.chatapp.com.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

import nus.iss.chatapp.com.server.models.MessageDetail;
import nus.iss.chatapp.com.server.utils.GenericAggregationOperation;
import nus.iss.chatapp.com.server.utils.Utils;

@Repository
public class MessageMongoRepository {
   
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Document> getFriendListMessages(){
        /*
        db.messages.aggregate([
            {
                $sort: {"msgTime": -1}
            }
            ,
            {
                $group: {_id: "$chatId", messages: {$push: "$$ROOT"}}
            }
            ,
            {
                $project: {"message": {$first: "$messages"}, _id: 0}
            }
            ,
            {
                $project: {
                    "chatId": "$message.chatId",
                    "senderId": "$message.senderId", 
                    "text" : "$message.text",
                    "msgTime": {$toString: "$message.msgTime"}
                    }
            }
            ])
         */
        final String MONGO_GROUP_CHATID = 
                """
                    {
                        $group: {_id: "$chatId", messages: {$push: "$$ROOT"}}
                    }
                """;
        final String MONGO_PROJECT_MESSAGE = 
                """
                    {
                        $project: {"message": {$first: "$messages"}, _id: 0}
                    }
                """;
        final String MONGO_PROJECT_RESULT = 
                """
                    {
                        $project: {
                            "chatId": "$message.chatId",
                            "senderId": "$message.senderId", 
                            "text" : "$message.text",
                            "msgTime": "$message.msgTime"
                            }
                    }
                """;

         SortOperation sortMsgTime = Aggregation.sort(Direction.DESC, "msgTime");
         AggregationOperation groupChatId = new GenericAggregationOperation(MONGO_GROUP_CHATID);
         AggregationOperation projectMessage = new GenericAggregationOperation(MONGO_PROJECT_MESSAGE);  
         AggregationOperation projectResult = new GenericAggregationOperation(MONGO_PROJECT_RESULT);    
         Aggregation pipeline = Aggregation.newAggregation(sortMsgTime,groupChatId,projectMessage,projectResult);
         System.out.println("pipeline>>>>"+pipeline);

        AggregationResults<Document> res = mongoTemplate.aggregate(pipeline, "messages", Document.class);
      
        return res.getMappedResults();
    }

    public List<MessageDetail> getMessagesByChatId(Integer chatId){
        /*
         db.getCollection("messages").find({"chatId" : NumberInt(7)})
         */

         Criteria criteria = Criteria.where("chatId").is(chatId);
         Query query = Query.query(criteria);

         List<Document> results = mongoTemplate.find(query, Document.class, "messages");

         List<MessageDetail> messages = results.stream() 
            .map(x -> Utils.toMessageDetail(x))
            .toList();

        return messages;
    }

    public void insertMessage(MessageDetail message){
        /*
         *db.messages.insertOne([{
            "chatId" : NumberInt(6),
            "senderId" : NumberInt(2),
            "text" : "hihihihihihi 5",
            "msgTime" : ISODate("2022-05-10T16:00:00.000+0000")
        }])
         */

         Document document = mongoTemplate
                                .insert(Utils.toDocument(message), "messages");
    }

    public void deleteMessages(Integer chatId){
        /*
         db.getCollection("messages").deleteMany(({"chatId" : NumberInt(4)}));
         */
        // Criteria criteria = Criteria.where("chatId").is(chatId);
        // Query query = Query.query(criteria);
        Query query = Query.query(
            Criteria.where("chatId").is(chatId));
        mongoTemplate.remove(query,"messages");

        // System.out.println("deleteMessage>>>"+ result.toString());
    }
}
