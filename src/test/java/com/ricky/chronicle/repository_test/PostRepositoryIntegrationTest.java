package com.ricky.chronicle.Repository;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ricky.chronicle.IntegrationTest;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.repository.PostRepository;
import com.ricky.chronicle.repository.UserRepository;

import jakarta.transaction.Transactional;

public class PostRepositoryIntegrationTest extends IntegrationTest{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void givenUserUsername_whenGetAllPostsByUsername_returnAllPostsByUser(){
        List<User> dbUsers = userRepository.findAll();
        User user = dbUsers.getFirst();
        String username = user.getUsername();

        List<Post> posts = postRepository.findAllPostsByUserUsername(username);

        posts.stream().findFirst().orElseThrow();

        posts.stream().forEach(post->{
            assertAll("check all posts for corresponding username", ()->{
            post.getPostsByUsers()
                .stream()
                .filter(up->up.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(
                    ()->new NoSuchElementException("feed doesn't match the username")
                );
        });
        });
    }

    @Test
    @Transactional
    public void givenUserId_whenGetAllPostsByUserId_returnAllPostsByUser(){
        List<User> dbUsers = userRepository.findAll();
        User user = dbUsers.getFirst();
        UUID userId = user.getId();

        List<Post> posts = postRepository.findAllPostsByUserId(userId);

        posts.stream().findFirst().orElseThrow();

        posts.stream().forEach(post->{
            assertAll("check all posts for corresponding username", ()->{
            post.getPostsByUsers()
                .stream()
                .filter(up->up.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(
                    ()->new NoSuchElementException("feed doesn't match the username")
                );
        });
        });
    }
}
