package com.ricky.chronicle.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    protected UserResponse userMapper(User user){
        return new UserResponse(
            user.getId(), 
            user.getUsername(), 
            user.getCreatedAt(), 
            user.getUpdatedAt(), 
            user.getLastLoggedInAt()
        );
    }

    public List<UserResponse> getAllUsers(){
        List<UserResponse> userResponseList = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users){
            userResponseList.add(userMapper(user));
        }
        return userResponseList;
    }

    public UserResponse getUserById(UUID id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new NoSuchElementException("user does not exists");
        }else{
            User user = optionalUser.get();
            return userMapper(user);
        }
    }

    public UserResponse createUser(CreateUserRequest request){
        String username = request.username();
        String rawPassword = request.rawPassword();
        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("username already exists");
        }
        String hashedPassword = authService.hashPassword(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setHashedPassword(hashedPassword);
        User savedUser = userRepository.save(user);
        
        return userMapper(savedUser);
    }

    public Optional<User> findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
