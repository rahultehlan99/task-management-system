package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.dto.UserSignUpDTO;
import com.example.taskmanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "User sign up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User sign up successful"),
            @ApiResponse(responseCode = "400", description = "User already exists")
    }
    )
    @SecurityRequirement(name = "") // No security for this endpoint
    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> userSignUp(@Validated @RequestBody UserSignUpDTO userSignUpDTO){
        log.info("Request received for new user registration");
        return ResponseEntity.ok(userService.signUpUser(userSignUpDTO.getUserName(), userSignUpDTO.getPassword()));
    }

    @Operation(summary = "User sign in")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "User sign in successful")
    )
    @SecurityRequirement(name = "") // No security for this endpoint
    @PostMapping(value = "/signIn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userLogin(@Validated @RequestBody UserSignUpDTO userSignUpDTO){
        log.info("Request received for new user registration");
        HttpHeaders httpHeaders = userService.logInUser(userSignUpDTO.getUserName(), userSignUpDTO.getPassword());
        return new ResponseEntity<>("Sign In Successful", httpHeaders, HttpStatus.OK);
    }

    @Operation(summary = "User sign out")
    @ApiResponses(
            @ApiResponse(responseCode = "200", description = "User sign out successful")
    )
    @PostMapping(value = "/signOut", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> userLogout(@RequestHeader(value = "Authorization") String jwtToken){
        log.info("Request received for new user registration");
        return ResponseEntity.ok(userService.logOutUser(jwtToken));
    }
}
