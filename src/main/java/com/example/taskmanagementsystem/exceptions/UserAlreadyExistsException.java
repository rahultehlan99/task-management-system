package com.example.taskmanagementsystem.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    private String userName;
    private String message;

    public UserAlreadyExistsException(String message, String userName) {
        super(message);
        this.message = message;
        this.userName = userName;
    }
}
