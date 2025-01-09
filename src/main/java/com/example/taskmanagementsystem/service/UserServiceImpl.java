package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.entity.Role;
import com.example.taskmanagementsystem.entity.Users;
import com.example.taskmanagementsystem.enums.USER_ROLES;
import com.example.taskmanagementsystem.exceptions.UserAlreadyExistsException;
import com.example.taskmanagementsystem.repository.RoleRepository;
import com.example.taskmanagementsystem.repository.UsersRepository;
import com.example.taskmanagementsystem.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    @Value("${default.user.password}")
    private String defaultUserPassword;

    @PostConstruct
    public void init() {
        roleRepository.saveAll(Arrays.asList(
                Role.builder()
                        .userRoles(USER_ROLES.ROLE_ADMIN)
                        .build(),
                Role.builder()
                        .userRoles(USER_ROLES.ROLE_USER)
                        .build()));
        Users users = new Users();
        users.setUserName("Rahul Tehlan");
        users.setPassword(defaultUserPassword);
        users.setMailId("rahul@gmail.com");
        users.setSignUpTime(LocalDateTime.now());
        users.setRoles(new HashSet<>());
        users.getRoles().add(roleRepository.findById(USER_ROLES.ROLE_ADMIN).orElse(Role.builder()
                .userRoles(USER_ROLES.ROLE_USER)
                .build()));
        usersRepository.save(users);
        log.info("Root user created!!\uD83D\uDE00");
    }

    @Override
    public String signUpUser(@NonNull String userName, @NonNull String password) {
        log.info("Checking user : {}", userName);
        Optional<Users> checkUser = usersRepository.findByUserName(userName);
        if (checkUser.isPresent()) {
            log.info("User already exists with given username");
            throw new UserAlreadyExistsException("User already exists", userName);
        }
        Users users = new Users();
        users.setUserName(userName);
        users.setPassword(passwordEncoder.encode(password));
        users.setSignUpTime(LocalDateTime.now());
        users.setMailId("abc.mail@com");
        users.setRoles(new HashSet<>());
        users.getRoles().add(roleRepository.findById(USER_ROLES.ROLE_USER).get());
        usersRepository.save(users);
        return "User Created";
    }

    @Override
    public HttpHeaders signInUser(String userName, String password) {
        // generate and pass JWT token to the user
        Users user = usersRepository.findByUserName(userName).orElseThrow(() ->
                new UsernameNotFoundException("User does not exist"));
        HttpHeaders httpHeaders = new HttpHeaders();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        httpHeaders.setBearerAuth(jwtUtil.generateToken(claims, userName));
        return httpHeaders;
    }

    @Override
    public String signOutUser(String userName, String password) {
        // invalidate the JWT token passed in request
        return null;
    }
}
