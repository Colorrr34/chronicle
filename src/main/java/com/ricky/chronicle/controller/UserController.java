package com.ricky.chronicle.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.dto.userFeed.CreateUserFeedRequest;
import com.ricky.chronicle.dto.userFeed.UserFeedResponse;
import com.ricky.chronicle.service.UserFeedService;
import com.ricky.chronicle.service.UserPostService;
import com.ricky.chronicle.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFeedService userFeedService;
    private final UserPostService userPostService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {        
        UserResponse response = userService.getUserById(userId);

        return ResponseEntity.ok(response);
    }
    

    @PostMapping
    public ResponseEntity<UserResponse> postUser(@RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/{userId}/feeds")
    public ResponseEntity<UserFeedResponse> postUserFeed(@PathVariable UUID userId, @RequestBody CreateUserFeedRequest request){
        String feedTitle = request.feedTitle();

        UserFeedResponse response = userFeedService.createUserFeed(userId, feedTitle);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId, @RequestBody String rawPassword){
        String message = userService.deleteUser(userId, rawPassword);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{userid}/feeds/{feedId}")
    public ResponseEntity<String> deleteUserFeed(@PathVariable UUID userId, @PathVariable UUID feedId){
        String message = userFeedService.deleteUserFeedByUserIdAndFeedId(userId, feedId);

        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public ResponseEntity<String> deleteUserPost(@PathVariable UUID userId, @PathVariable UUID postId){
        String message = userPostService.deleteUserPostByUserIdAndPostId(userId, postId);

        return ResponseEntity.ok(message);
    }
}
