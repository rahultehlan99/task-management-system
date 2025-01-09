package com.example.taskmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class Users extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private long userId;

    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String userName;

    @Column(name = "USER_PWD", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(name = "SIGN_UP_DATE", nullable = false, updatable = false)
    private LocalDateTime signUpTime;

    @Column(name = "LAST_SUCCESS_LOGIN")
    private LocalDateTime lastSuccessSignIn;

    @Column(name = "FAILED_ATTEMPTS")
    private long incorrectLoginAttemptsCount;

    @Column(name = "EMAIL_ID", nullable = false)
    private String mailId;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tasks> tasks = new ArrayList<>();
}
