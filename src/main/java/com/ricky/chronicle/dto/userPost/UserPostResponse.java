package com.ricky.chronicle.dto.userPost;

import java.util.UUID;

import com.ricky.chronicle.dto.post.PostSummary;
import com.ricky.chronicle.dto.user.UserSummary;

public record UserPostResponse(
    UUID id,
    UserSummary userSummary,
    PostSummary postSummary
) {
    
}
