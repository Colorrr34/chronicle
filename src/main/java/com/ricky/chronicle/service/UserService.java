package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.exception.InvalidArgumentException;
import com.ricky.chronicle.map.UserMapper;
import com.ricky.chronicle.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserResponse> response = users.stream().map(user->userMapper.toResponse(user)).toList();

        return response;
    }

    public UserResponse getUserById(UUID userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()){
            throw new NoSuchElementException("user does not exists");
        }
        User user = optionalUser.get();
        return userMapper.toResponse(user);
    }

    public UserResponse createUser(CreateUserRequest request){
        String username = request.username();
        String rawPassword = request.rawPassword();
        if (username.isBlank()){
            throw new InvalidArgumentException("username can't be blank");
        }
        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("username already exists");
        }
        String hashedPassword = authService.hashPassword(rawPassword);

        User user = userMapper.toEntity(request);
        user.setHashedPassword(hashedPassword);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponse findUserByUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            throw new NoSuchElementException("user not found");
        }
        User user = optionalUser.get();
        
        return userMapper.toResponse(user);
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
