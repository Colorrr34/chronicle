package com.ricky.chronicle.dto.user;

public record CreateUserReq(
    String username,
    String rawPassword
) {
}
