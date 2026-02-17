package com.ricky.chronicle.dto.post;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ricky.chronicle.dto.feed.FeedSummary;

public record PostResponse(
    UUID id,
    String title,
    String description,
    String url,
    FeedSummary feedSummary,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime publishedAt
) {
    
}
