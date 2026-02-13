package com.ricky.chronicle.map;

import org.springframework.stereotype.Component;

import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.entity.User;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        return new UserResponse(
            user.getId(), 
            user.getUsername(), 
            user.getCreatedAt(), 
            user.getUpdatedAt(), 
            user.getLastLoggedInAt()
        );
    }
}
