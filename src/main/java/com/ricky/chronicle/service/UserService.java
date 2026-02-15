package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.exception.InvalidArgumentException;
import com.ricky.chronicle.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUserById(UUID userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new NoSuchElementException("user does not exists");
        }else{
            return optionalUser.get();
        }
    }

    public User createUser(String username, String rawPassword){
        if (username.isBlank()){
            throw new InvalidArgumentException("username can't be blank");
        }
        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("username already exists");
        }
        String hashedPassword = authService.hashPassword(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setHashedPassword(hashedPassword);
        return userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public String deleteUser(UUID userId, String rawPassword){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new NoSuchElementException("User does not exists");
        }
        User user = optionalUser.get();
        if(!authService.verifyPassword(user.getHashedPassword(), rawPassword)){
            throw new IllegalArgumentException("password does not match");
        }
        userRepository.delete(user);
        return "User deleted";
    }
}
