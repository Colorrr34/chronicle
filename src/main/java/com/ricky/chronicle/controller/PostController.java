package com.ricky.chronicle.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricky.chronicle.dto.post.CreatePostRequest;
import com.ricky.chronicle.dto.post.CreatePostResponse;
import com.ricky.chronicle.dto.post.PostResponse;
import com.ricky.chronicle.service.PostService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        List<PostResponse> response = postService.getAllPosts();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(@PathVariable UUID userId) {
        List<PostResponse> response = postService.getPostsByUserId(userId);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID postId){
        PostResponse postResponse = postService.getPostById(postId);

        return ResponseEntity.ok(postResponse);
    }

    @PostMapping
    public ResponseEntity<CreatePostResponse> postPost(@RequestBody CreatePostRequest request){
        CreatePostResponse response = postService.createPost(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable UUID postId){
        String message = postService.deletePostById(postId);

        return ResponseEntity.ok(message);
    }
}
