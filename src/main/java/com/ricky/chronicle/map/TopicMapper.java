package com.ricky.chronicle.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricky.chronicle.dto.topic.TopicResponse;
import com.ricky.chronicle.entity.Topic;

@Mapper(componentModel = "spring")
public interface TopicMapper {
    TopicResponse toResponse(Topic topic);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "feeds",ignore = true)
    Topic toEntity(String topic);
}
