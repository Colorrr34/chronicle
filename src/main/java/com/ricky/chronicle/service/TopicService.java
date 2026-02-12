package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.repository.TopicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    public List<Topic> findAllTopics(){
        return topicRepository.findAll();
    };

    public Topic findTopicByTopic(String topicString){
        Optional<Topic> optionalTopic = topicRepository.findByTopic(topicString);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("Topic not found");
        }
        return optionalTopic.get();
    }

    public Topic findTopicById(UUID id){
        Optional<Topic> optionalTopic = topicRepository.findById(id);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("Topic not found");
        }
        return optionalTopic.get();
    }
}
