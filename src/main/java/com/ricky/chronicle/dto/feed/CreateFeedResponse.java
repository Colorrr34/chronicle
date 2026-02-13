package com.ricky.chronicle.dto.feed;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateFeedResponse(
    UUID id,
    String title,
    String topic,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    
}
