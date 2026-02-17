package com.ricky.chronicle.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricky.chronicle.dto.feed.CreateFeedRequest;
import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.dto.feed.FeedSummary;
import com.ricky.chronicle.entity.Feed;

@Mapper(componentModel = "spring")
public interface FeedMapper {
    @Mapping(source = "topic.topic", target = "topic")
    FeedResponse toResponse(Feed feed);

    @Mapping(source = "topic.topic",target = "topic")
    FeedSummary toSummary(Feed feed);

    @Mapping(target="id",ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "feedsByUsers",ignore = true)
    Feed ToEntity(CreateFeedRequest request);
}
