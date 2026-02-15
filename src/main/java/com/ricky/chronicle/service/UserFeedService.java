package com.ricky.chronicle.service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserFeed;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.UserFeedRepository;
import com.ricky.chronicle.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFeedService {
    private final UserFeedRepository userFeedRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    public UserFeed createUserFeed(UUID userId, String feedTitle){
        Optional<User> optionalUser = userRepository.findById(userId);
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

    public String deleteUserFeedByUserIdAndFeedId(UUID userId, UUID feedId){
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);

        if(optionalUser.isEmpty()){
            throw new NoSuchElementException("user not found");
        }
        if(optionalFeed.isEmpty()){
            throw new NoSuchElementException("feed not found");
        }

        userFeedRepository.deleteByUserAndFeed(optionalUser.get(), optionalFeed.get());
        return "userFeed deleted";
    }
}
