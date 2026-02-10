package com.ricky.chronicle.dto.user;

public record CreateUserRequest(
    String username,
    String rawPassword
) {
}
