package com.example.taskmanagementsystem.service;

import org.springframework.http.HttpHeaders;

public interface UserService {
    String signUpUser(String userName, String password);
    HttpHeaders signInUser(String userName, String password);
    String signOutUser(String userName, String password);
}
