package com.ricky.chronicle.dto.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePostRequest(
    UUID userId,
    String feedTitle,
    String title,
    String description,
    String url,
    LocalDateTime publishedAt
) {
    
}
