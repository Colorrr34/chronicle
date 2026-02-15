package com.ricky.chronicle.controller;

import java.util.ArrayList;
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
import com.ricky.chronicle.dto.feed.CreateFeedResponse;
import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.map.FeedMapper;
import com.ricky.chronicle.service.FeedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {
    private final FeedService feedService;
    private final FeedMapper feedMapper;

    @GetMapping
    public ResponseEntity<List<FeedResponse>> getAllFeeds(){
        List<Feed> feeds = feedService.FindAllFeeds();
        List<FeedResponse> responseList = new ArrayList<>();

        for (Feed feed: feeds){
            responseList.add(feedMapper.toResponse(feed));
        }

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Feed>> getAllFeedsByUserId(@PathVariable UUID userId){
        List<Feed> feeds = feedService.FindAllFeedsByUserId(userId);
        
        return ResponseEntity.ok(feeds);
    }
    
    @PostMapping
    public ResponseEntity<CreateFeedResponse> postFeed(@RequestBody CreateFeedRequest request){
        CreateFeedResponse response = feedService.createFeed(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<String> deleteFeedById(@PathVariable UUID feedId){
        String message = feedService.DeleteFeedById(feedId);

        return ResponseEntity.ok(message);
    }
}
