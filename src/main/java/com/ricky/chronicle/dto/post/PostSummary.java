package com.ricky.chronicle.dto.post;

import java.util.UUID;

public record PostSummary(
    UUID id,
    String title,
    String feedTitle,
    String url
) {
    
}
