package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.UserSignUpDTO;
import com.example.taskmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> userSignUp(@Validated @RequestBody UserSignUpDTO userSignUpDTO){
        log.info("Request received for new user registration");
        return ResponseEntity.ok(userService.signUpUser(userSignUpDTO.getUserName(), userSignUpDTO.getPassword()));
    }

    @PostMapping(value = "/signIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userLogin(@Validated @RequestBody UserSignUpDTO userSignUpDTO){
        log.info("Request received for new user registration");
        // TODO : generate and send jwt token in headers
        HttpHeaders httpHeaders = userService.signInUser(userSignUpDTO.getUserName(), userSignUpDTO.getPassword());
        return new ResponseEntity<>("Sign In Successful", httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/signOut", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userLogout(@Validated @RequestBody UserSignUpDTO userSignUpDTO){
        log.info("Request received for new user registration");
        // TODO : expire jwt token
        return ResponseEntity.ok(userService.signOutUser(userSignUpDTO.getUserName(), userSignUpDTO.getPassword()));
    }
}
