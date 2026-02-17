package com.ricky.chronicle.dto.user;

import java.util.UUID;

public record UserSummary(
    UUID id,
    String username
) {
    
}
