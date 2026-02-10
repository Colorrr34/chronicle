package com.ricky.chronicle.mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricky.chronicle.dto.feed.CreateFeedRequest;
import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.TopicRepository;
import com.ricky.chronicle.service.FeedService;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {
    @Mock
    FeedRepository mockFeedRepository;

    @Mock
    TopicRepository mockTopicRepository;

    @InjectMocks
    FeedService feedService;

    @Test
    void createFeed_shouldCreateFeedWithTopic_whenTopicExists(){
        String topicString = "test topic";
        String title = "test title";
        Feed mockFeed = new Feed();
        Topic mockTopic = new Topic();
        mockTopic.setTopic(topicString);
        mockFeed.setTopic(mockTopic);
        mockFeed.setTitle(title);

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.of(mockTopic));
        when(mockFeedRepository.save(any(Feed.class))).thenReturn(mockFeed);

        FeedResponse feedResponse = feedService.createFeed(new CreateFeedRequest(title, topicString));

        verify(mockFeedRepository,times(1)).save(argThat(
            feed->
            feed.getTitle().equals(title)&&
            feed.getTopic().equals(mockTopic)
        ));

        assertThat(feedResponse).isNotNull();
        assertThat(feedResponse.topic()).isEqualTo(topicString);
        assertThat(feedResponse.title()).isEqualTo(title);
    }

    @Test
    public void createFeed_shouldCreateTopic_whenTopicIsEmpty(){
        String topicString = "not exist";
        String title = "test title";
        Topic mockTopic = new Topic();
        Feed mockFeed = new Feed();
        mockTopic.setTopic(topicString);
        mockFeed.setTitle(title);
        mockFeed.setTopic(mockTopic);

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.empty());
        when(mockTopicRepository.save(any(Topic.class))).thenReturn(mockTopic);
        when(mockFeedRepository.save(any(Feed.class))).thenReturn(mockFeed);

        FeedResponse feedResponse = feedService.createFeed(new CreateFeedRequest(title, topicString));

        verify(mockTopicRepository,times(1)).findByTopic(topicString);
        verify(mockTopicRepository,times(1)).save(argThat(
            topic->
            topic.getTopic().equals(topicString)
        ));
        verify(mockFeedRepository,times(1)).save(argThat(
            feed->
            feed.getTopic().equals(mockTopic)&&
            feed.getTitle().equals(title)
        ));

        assertThat(feedResponse).isNotNull();
        assertThat(feedResponse.topic()).isEqualTo(topicString);
        assertThat(feedResponse.title()).isEqualTo(title);
    }

    @Test
    public void createFeed_shouldThrowException_whenTitleExists(){
        when(mockFeedRepository.findByTitle("existing title")).thenReturn(Optional.of(new Feed()));

        assertThrows(IllegalArgumentException.class, ()->{
            feedService.createFeed(new CreateFeedRequest("existing title", "topic"));
        });

        verify(mockFeedRepository,times(0)).save(any(Feed.class));
    }
}
