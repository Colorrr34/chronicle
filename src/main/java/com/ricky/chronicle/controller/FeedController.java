package com.ricky.chronicle.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ricky.chronicle.dto.feed.CreateFeedRequest;
import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.service.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {
    private final FeedService feedService;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> getAllFeeds(){
        List<FeedResponse> response = feedService.FindAllFeeds();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<FeedResponse>> getAllFeedsByUserId(@PathVariable UUID userId){
        List<FeedResponse> response = feedService.FindAllFeedsByUserId(userId);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<FeedResponse> postFeed(@RequestBody CreateFeedRequest request){
        FeedResponse response = feedService.createFeed(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<String> deleteFeedById(@PathVariable UUID feedId){
        String message = feedService.DeleteFeedById(feedId);

        return ResponseEntity.ok(message);
    }
}
