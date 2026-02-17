package com.ricky.chronicle.dto.userFeed;

import java.util.UUID;

import com.ricky.chronicle.dto.feed.FeedSummary;
import com.ricky.chronicle.dto.user.UserSummary;

public record UserFeedResponse(
    UUID id,
    UserSummary userSummary,
    FeedSummary feedSummary
) {
    
}
