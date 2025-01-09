package com.example.taskmanagementsystem.security;

import com.example.taskmanagementsystem.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomAuthenticatorManager implements AuthenticationManager {

    private final CustomUserDetailsProvider customUserDetailsProvider;

    private final UsersRepository usersRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String passWord = authentication.getCredentials().toString();

        UserDetails userDetails = customUserDetailsProvider.loadUserByUsername(userName);

        if (Objects.isNull(userDetails)) {
            throw new UsernameNotFoundException("No user with given user name");
        }

        if (passWord.equalsIgnoreCase(userDetails.getPassword())) {
            increaseFailedAttempts(userName);
            throw new BadCredentialsException("Password does not match");
        }

        clearFailedPasswordAttempts(userName);
        return new UsernamePasswordAuthenticationToken(userName, passWord, userDetails.getAuthorities());
    }

    private void clearFailedPasswordAttempts(String userName) {
        usersRepository.clearFailedPasswordAttempts(userName);
    }

    private void increaseFailedAttempts(String userName) {
        usersRepository.increaseFailedAttempts(userName);
    }
}
