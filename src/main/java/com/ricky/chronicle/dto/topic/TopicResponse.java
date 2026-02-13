package com.ricky.chronicle.dto.topic;

import java.util.UUID;

public record TopicResponse(
    UUID id,
    String topic
) {
    
}
