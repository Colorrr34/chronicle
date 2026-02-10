package com.ricky.chronicle.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.TopicRepository;

@Service
public class FeedService {
    private final FeedRepository feedRepository;
    private final TopicRepository topicRepository;

    public FeedService(FeedRepository feedRepository, TopicRepository topicRepository){
        this.feedRepository = feedRepository;
        this.topicRepository = topicRepository;
    }

    public Feed createFeed(String title, String topicString){
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

        return feedRepository.save(feed);
    }

    public Optional<Feed> FindFeedByTitle(String title){
        return feedRepository.findByTitle(title);
    }
}
