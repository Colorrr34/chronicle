package com.ricky.chronicle.dto.user;

import java.util.UUID;

public record CreateUserReq(
    UUID id,
    String username,
    String password
) {
    
}
