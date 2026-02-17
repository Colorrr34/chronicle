package com.ricky.chronicle.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricky.chronicle.dto.userFeed.UserFeedResponse;
import com.ricky.chronicle.entity.UserFeed;

@Mapper(componentModel = "spring", uses = {UserMapper.class, FeedMapper.class})
public interface UserFeedMapper {
    @Mapping(source = "user", target = "userSummary")
    @Mapping(source = "feed", target = "feedSummary")
    UserFeedResponse toResponse(UserFeed UserFeed);
}
