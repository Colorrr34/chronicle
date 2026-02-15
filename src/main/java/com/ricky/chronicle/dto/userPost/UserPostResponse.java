package com.ricky.chronicle.dto.userPost;

import java.util.UUID;

public record UserPostResponse(
    UUID id,
    UUID userId,
    UUID postId
) {
    
}
