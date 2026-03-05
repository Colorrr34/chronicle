package com.ricky.chronicle.dto.auth;

import java.util.UUID;

public record LoginResponse(
    String token,
    UUID userId
) {
    
}
