package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.dto.feed.CreateFeedRequest;
import com.ricky.chronicle.dto.feed.CreateFeedResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.TopicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final TopicRepository topicRepository;

    public CreateFeedResponse createFeed(CreateFeedRequest request){
        String title = request.title();
        String topicString = request.topicString();
        if (feedRepository.findByTitle(title).isPresent()){
            throw new IllegalArgumentException("title already exists");
        }
        Optional<Topic> optionalTopic = topicRepository.findByTopic(topicString);
        Topic dbTopic = new Topic();
        if (optionalTopic.isEmpty()){
            Topic topic = new Topic();
            topic.setTopic(topicString);
            dbTopic = topicRepository.save(topic);
        } else{
            dbTopic = optionalTopic.get();
        }
        
        Feed feed = new Feed();
        feed.setTitle(title);
        feed.setTopic(dbTopic);

        Feed savedFeed = feedRepository.save(feed);
        return new CreateFeedResponse(
            savedFeed.getId(),
            savedFeed.getTitle(), 
            savedFeed.getTopic().getTopic(), 
            savedFeed.getCreatedAt(), 
            savedFeed.getUpdatedAt()
        );
    }

    public Optional<Feed> FindFeedByTitle(String title){
        return feedRepository.findByTitle(title);
    }

    public List<Feed> FindAllFeeds(){
        return feedRepository.findAll();
    }

    public List<Feed> FindAllFeedsByUserId(UUID userId){
        return feedRepository.findAllFeedsByUserId(userId);
    }

    public String DeleteFeedById(UUID feedId){
        Optional<Feed> optionalFeed = feedRepository.findById(feedId);
        if(optionalFeed.isEmpty()){
            throw new NoSuchElementException("feed not found ");
        }

        feedRepository.delete(optionalFeed.get());
        return "feed deleted";
    }
}
