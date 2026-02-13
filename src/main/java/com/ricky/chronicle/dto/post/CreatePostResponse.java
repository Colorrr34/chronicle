package com.ricky.chronicle.dto.post;

import java.util.UUID;

public record CreatePostResponse(
    String message,
    UUID userId,
    UUID userPostId,
    PostResponse postResponse
) {
    
}
