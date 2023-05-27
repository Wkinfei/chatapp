package nus.iss.chatapp.com.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nus.iss.chatapp.com.server.exceptions.AddFriendException;
import nus.iss.chatapp.com.server.exceptions.DeleteUserDataException;

import nus.iss.chatapp.com.server.exceptions.ErrorsResponse;

@RestControllerAdvice
public class ErrorController {
    
    @ExceptionHandler(value={DeleteUserDataException.class})
    public ResponseEntity<String> handleDeleteException(DeleteUserDataException e) {

       ErrorsResponse error = new ErrorsResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return ResponseEntity
                .status(error.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(error.toJson().toString());
    }


    @ExceptionHandler(value={AddFriendException.class})
    public ResponseEntity<String> handleAddFriendException(AddFriendException e) {

        ErrorsResponse error = new  ErrorsResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
       

        return ResponseEntity
                .status(error.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(error.toJson().toString());
    }
}
