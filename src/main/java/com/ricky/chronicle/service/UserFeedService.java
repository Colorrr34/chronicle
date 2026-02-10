package com.ricky.chronicle.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserFeed;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.UserFeedRepository;
import com.ricky.chronicle.repository.UserRepository;

@Service
public class UserFeedService {
    private final UserFeedRepository userFeedRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public UserFeedService(
        UserFeedRepository userFeedRepository,
        UserRepository userRepository,
        FeedRepository feedRepository
    ){
        this.userFeedRepository = userFeedRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
    }

    public UserFeed createUserFeed(String username, String feedTitle){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()){
            throw new IllegalArgumentException("user not found");
        }
        Optional<Feed> optionalFeed = feedRepository.findByTitle(feedTitle);
        if(optionalFeed.isEmpty()){
            throw new IllegalArgumentException("feed not found");
        }
        User user = optionalUser.get();
        Feed feed = optionalFeed.get();
        UserFeed userFeed = new UserFeed();
        userFeed.setUser(user);
        userFeed.setFeed(feed);

        return userFeedRepository.save(userFeed);
    }
}
