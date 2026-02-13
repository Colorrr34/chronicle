package com.ricky.chronicle.dto.post;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponse(
    UUID id,
    String title,
    String description,
    String url,
    String feedTitle,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime publishedAt
) {
    
}
