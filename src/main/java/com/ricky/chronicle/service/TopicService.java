package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.repository.TopicRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;

    public List<Topic> getAllTopics(){
        return topicRepository.findAll();
    };

    public Topic getTopicByTopic(String topicString){
        Optional<Topic> optionalTopic = topicRepository.findByTopic(topicString);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("Topic not found");
        }
        return optionalTopic.get();
    }

    public Topic getTopicById(UUID id){
        Optional<Topic> optionalTopic = topicRepository.findById(id);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("Topic not found");
        }
        return optionalTopic.get();
    }

    @Transactional
    public Topic createTopic(String topicString){
        Topic topic = new Topic();
        topic.setTopic(topicString);
        return topicRepository.save(topic);
    }

    @Transactional
    public String deleteTopicByTopic(String topicString){
        Optional<Topic> optionalTopic = topicRepository.findByTopic(topicString);
        if (optionalTopic.isEmpty()){
            throw new NoSuchElementException("topic not found");
        }
        topicRepository.delete(optionalTopic.get());
        return "topic deleted";
    }
}
