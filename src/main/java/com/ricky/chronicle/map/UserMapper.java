package com.ricky.chronicle.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.dto.user.UserSummary;
import com.ricky.chronicle.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);

    UserSummary toSummary(User user);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "hashedPassword", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLoggedInAt", ignore = true)
    @Mapping(target = "savedFeeds", ignore = true)
    @Mapping(target = "savedPosts",ignore = true)
    User toEntity(CreateUserRequest request);
}