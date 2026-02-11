package com.ricky.chronicle.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.dto.userFeed.CreateUserFeedRequest;
import com.ricky.chronicle.dto.userFeed.UserFeedResponse;
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

    public UserFeedResponse createUserFeed(CreateUserFeedRequest request){
        UUID userId = request.userId();
        String feedTitle = request.feedTitle();
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

        UserFeed responseUserFeed = userFeedRepository.save(userFeed);
        return new UserFeedResponse(
            responseUserFeed.getId(), 
            responseUserFeed.getUser().getId(), 
            responseUserFeed.getFeed().getId(), 
            responseUserFeed.getFeed().getTitle()
        );
    }
}
