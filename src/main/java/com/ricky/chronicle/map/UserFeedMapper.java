package com.ricky.chronicle.map;

import org.springframework.stereotype.Component;

import com.ricky.chronicle.dto.userFeed.UserFeedResponse;
import com.ricky.chronicle.entity.UserFeed;

@Component
public class UserFeedMapper {
    public UserFeedResponse toResponse(UserFeed userFeed){
        return new UserFeedResponse(
            userFeed.getId(), 
            userFeed.getUser().getId(), 
            userFeed.getFeed().getId()
        );
    }
}
