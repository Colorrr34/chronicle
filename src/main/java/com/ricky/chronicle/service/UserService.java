package com.ricky.chronicle.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService){
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public User createUser(String username, String rawPassword){
        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("username already exists");
        }
        String hashedPassword = authService.hashPassword(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setHashedPassword(hashedPassword);
        User dbUser = userRepository.save(user);

        return dbUser;
    }

    public Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
