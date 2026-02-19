package com.ricky.chronicle.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public Boolean verifyPassword(String rawPassword, String hashedPassword){
        System.out.println(hashedPassword);
        System.out.println(passwordEncoder.encode(rawPassword));
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}   
