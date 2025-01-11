package com.example.taskmanagementsystem.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleUserAlreadyExists(UserAlreadyExistsException existsException) {
        log.error("User already exists : {}", existsException.getUserName());
        return "User already exists";
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object genericException(Exception exception) {
        log.error("Generic exception occurred : {}", exception.getMessage());
        return "Internal Exception occurred";
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object noSuchElementException(NoSuchElementException exception) {
        log.error("No such element exception occurred : {}", exception.getMessage());
        return "No such element exception occurred";
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object accessDeniedException(AccessDeniedException exception) {
        log.error("You do not have access for the operation : {}", exception.getMessage());
        return "You do not have access for the operation";
    }

}
