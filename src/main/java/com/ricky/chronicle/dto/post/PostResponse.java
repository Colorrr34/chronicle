package com.ricky.chronicle.dto.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponse(
    UUID userId,
    UUID id,
    String feedTitle,
    String title,
    String description,
    String url,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime publishedAt,
    UUID userPostId
) {
    
}
