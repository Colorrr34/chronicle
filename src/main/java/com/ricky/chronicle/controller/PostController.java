package com.ricky.chronicle.controller;

import java.util.ArrayList;
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
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.map.PostMapper;
import com.ricky.chronicle.service.PostService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        List<PostResponse> postsResponseList = new  ArrayList<>();
        List<Post> posts = postService.getAllPosts();
        for (Post post: posts){
            postsResponseList.add(postMapper.toResponse(post));
        }

        return ResponseEntity.ok(postsResponseList);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(@PathVariable UUID userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        List<PostResponse> postsResponseList = new ArrayList<>();

        for (Post post:posts){
            postsResponseList.add(postMapper.toResponse(post));
        }

        return ResponseEntity.ok(postsResponseList);
    }
    

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID postId){
        PostResponse postResponse = postMapper.toResponse(postService.getPostById(postId));

        return ResponseEntity.ok(postResponse);
    }

    @PostMapping
    public ResponseEntity<CreatePostResponse> postPost(@RequestBody CreatePostRequest request){
        CreatePostResponse response = postService.createPost(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
