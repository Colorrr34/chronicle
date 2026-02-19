package com.ricky.chronicle.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ricky.chronicle.dto.topic.CreateTopicRequest;
import com.ricky.chronicle.dto.topic.TopicResponse;
import com.ricky.chronicle.service.TopicService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicResponse>> getAllTopics() {
        List<TopicResponse> response = topicService.getAllTopics();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<TopicResponse> postTopic(@RequestBody CreateTopicRequest request){
        String topic = request.topic();

        TopicResponse response = topicService.createTopic(topic);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{topic}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable String topic){
        topicService.deleteTopicByTopic(topic);
    }
}
