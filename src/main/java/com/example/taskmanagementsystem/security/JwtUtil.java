package com.example.taskmanagementsystem.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    public String getUserNameFromToken(String token) {
        return "rahul";
    }

}
