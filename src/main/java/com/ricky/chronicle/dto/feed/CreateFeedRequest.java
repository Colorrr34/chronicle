package com.ricky.chronicle.dto.feed;

public record CreateFeedRequest(
    String title,
    String topicString
) {
    
}
