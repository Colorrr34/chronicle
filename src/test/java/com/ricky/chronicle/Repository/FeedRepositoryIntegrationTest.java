package com.ricky.chronicle.Repository;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import com.ricky.chronicle.IntegrationTest;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.UserRepository;

import jakarta.transaction.Transactional;

public class FeedRepositoryIntegrationTest extends IntegrationTest{
    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void givenUserUsername_whenGetAllFeeds_returnAllFeedsByUser(){
        List<User> dbUsers = userRepository.findAll();
        User user = dbUsers.getFirst();
        String username = user.getUsername();

        List<Feed> feeds = feedRepository.findAllFeedsByUserUsername(username);

        Feed firstFeed = feeds.stream().findFirst().orElseThrow();

        assertAll("check all feeds for corresponding username", ()->{
            firstFeed.getFeedsByUsers()
                .stream()
                .filter(uf->uf.getUser().getUsername().equals(username))
                .findFirst()
                .orElseThrow(
                    ()->new NoSuchElementException("feed doesn't match the username")
                );
        });     
    }
}
