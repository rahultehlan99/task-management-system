package com.example.taskmanagementsystem.security;

import com.example.taskmanagementsystem.entity.Users;
import com.example.taskmanagementsystem.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsProvider implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userOptional = usersRepository.findByUserName(username);
        return userOptional.map(users -> User
                .withUsername(users.getUserName())
                .password(users.getPassword())
                .roles("USER_ROLE")
                .build()).orElse(null);
    }
}
