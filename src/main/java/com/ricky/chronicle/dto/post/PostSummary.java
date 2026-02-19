package com.ricky.chronicle.dto.post;

import java.util.UUID;

import com.ricky.chronicle.dto.feed.FeedSummary;

public record PostSummary(
    UUID id,
    String title,
    FeedSummary feedSummary,
    String url
) {
    
}
