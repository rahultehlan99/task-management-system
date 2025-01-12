package com.example.taskmanagementsystem.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpDTO {
    @NonNull
    private String userName;
    @NonNull
    private String password;
}
