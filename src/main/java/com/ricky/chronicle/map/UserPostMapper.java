package com.ricky.chronicle.map;

import com.ricky.chronicle.dto.userPost.UserPostResponse;
import com.ricky.chronicle.entity.UserPost;

public class UserPostMapper {
    public UserPostResponse toResponse(UserPost userPost){
        return new UserPostResponse(
            userPost.getId(), 
            userPost.getUser().getId(), 
            userPost.getPost().getId()
        );
    }
}
