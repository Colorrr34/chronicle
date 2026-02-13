package com.ricky.chronicle.dto.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateUserResponse(
    UUID id,
    String username,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime lastLoggedInAt
) {
    
}
