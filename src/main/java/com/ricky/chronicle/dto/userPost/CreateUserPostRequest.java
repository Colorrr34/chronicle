package com.ricky.chronicle.dto.userPost;

import java.util.UUID;

public record CreateUserPostRequest(
    UUID userId,
    UUID postId
) {
    
}
