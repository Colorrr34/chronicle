package com.ricky.chronicle.dto.userFeed;

import java.util.UUID;

public record UserFeedResponse(
    UUID userFeedId,
    UUID userId,
    UUID feedId,
    String feedTitle
) {
    
}
