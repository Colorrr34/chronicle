package com.ricky.chronicle.dto.userFeed;

import java.util.UUID;

public record CreateUserFeedRequest(
    UUID userId,
    String feedTitle
) {
    
}
