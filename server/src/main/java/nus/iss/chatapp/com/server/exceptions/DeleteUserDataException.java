package nus.iss.chatapp.com.server.exceptions;

public class DeleteUserDataException extends RuntimeException{
    
    public DeleteUserDataException(){
        super();
    }

    public DeleteUserDataException(String message){
        super(message);
    }
}
