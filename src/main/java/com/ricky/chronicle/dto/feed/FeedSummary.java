package com.ricky.chronicle.dto.feed;

import java.util.UUID;

public record FeedSummary(UUID id,
    String title,
    String topic) {
    
}
