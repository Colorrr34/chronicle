package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.dto.feed.CreateFeedRequest;
import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.map.FeedMapper;
import com.ricky.chronicle.map.TopicMapper;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.TopicRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final TopicRepository topicRepository;
    private final FeedMapper feedMapper;
    private final TopicMapper topicMapper;

    @Transactional
    public FeedResponse createFeed(CreateFeedRequest request){
        String title = request.title();
        String topicString = request.topicString();
        if (feedRepository.findByTitle(title).isPresent()){
            throw new IllegalArgumentException("title already exists");
        }
        Optional<Topic> optionalTopic = topicRepository.findByTopic(topicString);
        Topic topic;
        if (optionalTopic.isEmpty()){
            topic = topicMapper.toEntity(topicString);
            topic = topicRepository.save(topic);
        } else{
            topic = optionalTopic.get();
        }
        
        Feed feed = feedMapper.ToEntity(request);
        feed.setTopic(topic);

        Feed savedFeed = feedRepository.save(feed);
        return feedMapper.toResponse(savedFeed);
    }

    public FeedResponse FindFeedByTitle(String title){
        Optional<Feed> optionalFeed = feedRepository.findByTitle(title);
        if(optionalFeed.isEmpty()){
            throw new NoSuchElementException("feed not found");
        }
        Feed feed = optionalFeed.get();
        return feedMapper.toResponse(feed);
    }

    public List<FeedResponse> FindAllFeeds(){
        List<Feed> feeds = feedRepository.findAll();
        List<FeedResponse> response = feeds.stream().map(feed->feedMapper.toResponse(feed)).toList();

        return response;
    }

    public List<FeedResponse> FindAllFeedsByUserId(UUID userId){
        List<Feed> feeds = feedRepository.findAllFeedsByUserId(userId);
        List<FeedResponse> response = feeds.stream().map(feed->feedMapper.toResponse(feed)).toList();

        return response;
    }

    @Transactional
    public void DeleteFeedById(UUID feedId){
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        if(optionalFeed.isEmpty()){
            throw new NoSuchElementException("feed not found ");
        }

        feedRepository.delete(optionalFeed.get());
    }
}
