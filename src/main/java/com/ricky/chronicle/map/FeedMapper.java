package com.ricky.chronicle.map;

import org.springframework.stereotype.Component;

import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.entity.Feed;

@Component
public class FeedMapper {
    public FeedResponse toResponse(Feed feed){
        return new FeedResponse(
            feed.getId(), 
            feed.getTitle(), 
            feed.getTopic().getTopic(), 
            feed.getCreatedAt(), 
            feed.getUpdatedAt()
        );
    }
}
