package com.example.taskmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private long userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_PWD")
    private String password;

    @Column(name = "SIGN_UP_DATE")
    private LocalDateTime signUpTime;

    @Column(name = "LAST_SUCCESS_LOGIN")
    private LocalDateTime lastSuccessSignIn;

    @Column(name = "FAILED_ATTEMPTS")
    private long incorrectLoginAttemptsCount;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tasks> tasks = new ArrayList<>();
}
