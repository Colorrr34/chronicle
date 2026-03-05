package com.ricky.chronicle.dto.auth;

public record LoginRequest(
    String username,
    String rawPassword
) {
    
}
