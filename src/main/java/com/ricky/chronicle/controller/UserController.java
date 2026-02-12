package com.ricky.chronicle.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.service.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService= userService;
    }
   
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> usersResponseList = userService.getAllUsers();

        return ResponseEntity.ok(usersResponseList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        UserResponse userResponse = userService.getUserById(userId);

        return ResponseEntity.ok(userResponse);
    }
    

    @PostMapping
    public ResponseEntity<UserResponse> postUser(@RequestBody CreateUserRequest request) {
        if (request.username().isBlank()||request.rawPassword().isBlank()){
            throw new IllegalArgumentException("missing body");
        }

        UserResponse userResponse = userService.createUser(request);

        

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
    
}
