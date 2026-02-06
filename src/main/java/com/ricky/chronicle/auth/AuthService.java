package com.ricky.chronicle.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public Boolean verifyPassword(String hashedPassword, String password){
        return passwordEncoder.matches(password, hashedPassword);
    }
}   
