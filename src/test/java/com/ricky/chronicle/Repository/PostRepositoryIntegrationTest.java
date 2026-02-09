package com.ricky.chronicle.Repository;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;

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
    public void givenUserUsername_whenGetAllPosts_returnAllPostsByUser(){
        List<User> dbUsers = userRepository.findAll();
        User user = dbUsers.getFirst();
        String username = user.getUsername();

        List<Post> posts = postRepository.findAllPostsByUserUsername(username);

        Post firstPost = posts.stream().findFirst().orElseThrow();

        assertAll("check all posts for corresponding username", ()->{
            firstPost.getPostsByUsers()
                .stream()
                .filter(up->up.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(
                    ()->new NoSuchElementException("feed doesn't match the username")
                );
        });
    }
}
