package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.dto.topic.TopicResponse;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.map.TopicMapper;
import com.ricky.chronicle.repository.TopicRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public List<TopicResponse> getAllTopics(){
        List<Topic> topics = topicRepository.findAll();
        List<TopicResponse> response = topics.stream().map(topic->topicMapper.toResponse(topic)).toList();
        return response;
    };

    public TopicResponse getTopicByTopic(String topicString){
        Optional<Topic> optionalTopic = topicRepository.findByTopic(topicString);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("Topic not found");
        }
        Topic topic = optionalTopic.get();
        return topicMapper.toResponse(topic);
    }

    public TopicResponse getTopicById(UUID id){
        Optional<Topic> optionalTopic = topicRepository.findById(id);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("Topic not found");
        }
        Topic topic = optionalTopic.get();
        return topicMapper.toResponse(topic);
    }

    @Transactional
    public TopicResponse createTopic(String topicString){
        if(!topicRepository.findByTopic(topicString).isEmpty()){
            throw new IllegalArgumentException("topic already exists");
        }
        Topic topic = new Topic();
        topic.setTopic(topicString);
        return topicMapper.toResponse(topicRepository.save(topic));
    }

    @Transactional
    public void deleteTopicByTopic(String topicString){
        Optional<Topic> optionalTopic = topicRepository.findByTopic(topicString);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("topic not found");
        }
        topicRepository.delete(optionalTopic.get());
    }
}
