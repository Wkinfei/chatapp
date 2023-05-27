package nus.iss.chatapp.com.server.exceptions;




public class AddFriendException extends RuntimeException{
    public AddFriendException(){
        super();
    }

    public AddFriendException(String message){
        super(message);
    }
}
