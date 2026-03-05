package com.ricky.chronicle.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ricky.chronicle.dto.auth.LoginRequest;
import com.ricky.chronicle.dto.auth.LoginResponse;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.exception.InvalidArgumentException;
import com.ricky.chronicle.service.JwtService;
import com.ricky.chronicle.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtService jwtService;

    public String hashPassword(String password){
        return passwordEncoder.encode(password);
    }

    public Boolean verifyPassword(String rawPassword, String hashedPassword){
        System.out.println(hashedPassword);
        System.out.println(passwordEncoder.encode(rawPassword));
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    public LoginResponse loginHandler(LoginRequest request){
        String username = request.username();
        String rawPassword = request.rawPassword();

        User user = userService.getUserByUsername(username);

        if(!verifyPassword(rawPassword, user.getHashedPassword())){
            throw new InvalidArgumentException("password doesn't match");
        }

        String token = jwtService.generateToken(user.getId());

        return new LoginResponse(token, user.getId());
    }
}   
