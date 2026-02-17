package com.ricky.chronicle.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricky.chronicle.dto.userPost.UserPostResponse;
import com.ricky.chronicle.entity.UserPost;

@Mapper(componentModel = "spring",uses = {UserMapper.class,PostMapper.class})
public interface UserPostMapper {
    @Mapping(source = "user",target = "userSummary")
    @Mapping(source = "post", target = "postSummary")
    UserPostResponse toResponse(UserPost userPost);
}
