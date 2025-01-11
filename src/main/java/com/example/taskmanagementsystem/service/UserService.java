package com.example.taskmanagementsystem.service;

import org.springframework.http.HttpHeaders;

public interface UserService {
    String signUpUser(String userName, String password);
    HttpHeaders logInUser(String userName, String password);
    String logOutUser(String jwtToken);
}
