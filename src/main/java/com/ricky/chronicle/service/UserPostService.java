package com.ricky.chronicle.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserPost;
import com.ricky.chronicle.repository.PostRepository;
import com.ricky.chronicle.repository.UserPostRepository;
import com.ricky.chronicle.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPostService {
    private final UserPostRepository userPostRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserPost createUserPost(UUID userId, UUID postId){
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalUser.isEmpty()){
            throw new NoSuchElementException("user not found");
        }
        if(optionalPost.isEmpty()){
            throw new NoSuchElementException("post not found");
        }
        UserPost userPost = new UserPost();
        userPost.setUser(optionalUser.get());
        userPost.setPost(optionalPost.get());

        UserPost savedUserPost = userPostRepository.save(userPost);

        return savedUserPost;
    }

    public String deleteUserPostByUserIdAndPostId(UUID userId, UUID postId){
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalUser.isEmpty()){
            throw new NoSuchElementException("user not found");
        }
        if(optionalPost.isEmpty()){
            throw new NoSuchElementException("post not found");
        }

        userPostRepository.deleteByUserAndPost(optionalUser.get(), optionalPost.get());
        return "userPost deleted";
    }
}
