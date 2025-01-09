package com.example.taskmanagementsystem.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException existsException) {
        log.error("User already exists : {}", existsException.getUserName());
        return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> genericException(Exception exception) {
        log.error("Generic exception occurred : {}", exception.getMessage());
        return new ResponseEntity<>("Internal Exception occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
