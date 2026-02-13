package com.ricky.chronicle.map;

import org.springframework.stereotype.Component;

import com.ricky.chronicle.dto.topic.TopicResponse;
import com.ricky.chronicle.entity.Topic;

@Component
public class TopicMapper {
    public TopicResponse toResponse(Topic topic){
        return new TopicResponse(topic.getId(), topic.getTopic());
    }
}
