package com.ricky.chronicle.service_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.ricky.chronicle.dto.feed.CreateFeedRequest;
import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.map.FeedMapper;
import com.ricky.chronicle.map.TopicMapper;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.TopicRepository;
import com.ricky.chronicle.service.FeedService;
import com.ricky.chronicle.util.FeedBuilder;
import com.ricky.chronicle.util.TopicBuilder;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {
    @Mock
    FeedRepository mockFeedRepository;

    @Mock
    TopicRepository mockTopicRepository;

    @InjectMocks
    FeedService feedService;

    @Mock
    FeedMapper mockFeedMapper;
    
    @Mock
    TopicMapper mockTopicMapper;

    @Test
    void createFeed_shouldCreateFeedWithTopic_whenTopicExists(){
        String topicString = "test topic";
        String title = "test title";
        Topic mockTopic = new TopicBuilder().withTopic(topicString).build();
        Feed mockFeed = new FeedBuilder().withTitleAndTopic(title, topicString).build();
        CreateFeedRequest request = new CreateFeedRequest(title, topicString);
        FeedResponse response = new FeedResponse(null, title, topicString, null, null);

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.of(mockTopic));
        when(mockFeedRepository.save(any(Feed.class))).thenReturn(mockFeed);
        when(mockFeedMapper.ToEntity(request)).thenReturn(mockFeed);
        when(mockFeedMapper.toResponse(mockFeed)).thenReturn(response);

        FeedResponse feedResponse = feedService.createFeed(request);

        verify(mockFeedRepository,times(1)).save(argThat(
            feed->
            feed.getTitle().equals(title)&&
            feed.getTopic().equals(mockTopic)
        ));

        assertThat(feedResponse).isNotNull();
        assertThat(feedResponse).isEqualTo(response);
    }

    @Test
    void createFeed_shouldCreateTopic_whenTopicIsEmpty(){
        String topicString = "not exist";
        String title = "test title";
        Topic mockTopic = new TopicBuilder().withTopic(topicString).build();
        Feed mockFeed = new FeedBuilder().withTitleAndTopic(title, topicString).build();
        CreateFeedRequest request = new CreateFeedRequest(title, topicString);
        FeedResponse response = new FeedResponse(UUID.randomUUID(), title, topicString, LocalDateTime.now(), LocalDateTime.now());

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.empty());
        when(mockTopicRepository.save(any(Topic.class))).thenReturn(mockTopic);
        when(mockFeedRepository.save(any(Feed.class))).thenReturn(mockFeed);
        when(mockFeedMapper.ToEntity(request)).thenReturn(mockFeed);
        when(mockFeedMapper.toResponse(mockFeed)).thenReturn(response);
        when(mockTopicMapper.toEntity(topicString)).thenReturn(mockTopic);

        FeedResponse feedResponse = feedService.createFeed(request);

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
    void createFeed_shouldThrowException_whenTitleExists(){
        when(mockFeedRepository.findByTitle("existing title")).thenReturn(Optional.of(new Feed()));

        assertThrows(IllegalArgumentException.class, ()->{
            feedService.createFeed(new CreateFeedRequest("existing title", "topic"));
        });

        verify(mockFeedRepository,times(0)).save(any(Feed.class));
    }

    @Test
    public void findAllFeeds_shouldReturnAListOfFeedResponse(){
        List<Feed> feeds = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            Feed feed = new FeedBuilder().withTitle("title "+i).build();
            feeds.add(feed);
        });

        when(mockFeedRepository.findAll()).thenReturn(feeds);
        when(mockFeedMapper.toResponse(any(Feed.class))).thenAnswer(invocation->{
            Feed f = invocation.getArgument(0);
            return new FeedResponse(null, f.getTitle(), null, null, null);
        });

        List<FeedResponse> response = feedService.FindAllFeeds();

        verify(mockFeedRepository,times(1)).findAll();
        verify(mockFeedMapper,times(10)).toResponse(any(Feed.class));

        assertAll(()->{
            assertThat(response).isNotNull();
            response.stream().forEach(feedResponse->{
                assertThat(feedResponse).isNotNull();
            });
        });
    }

    @Test
    void findFeedByTitle_shouldReturnAFeedResponse_whenGivenATitle(){
        String title = "title";
        Feed feed = new FeedBuilder().withTitle(title).build();
        FeedResponse feedResponse = new FeedResponse(null, title, title, null, null);

        when(mockFeedRepository.findByTitle(title)).thenReturn(Optional.of(feed));
        when(mockFeedMapper.toResponse(feed)).thenReturn(feedResponse);

        FeedResponse response = feedService.FindFeedByTitle(title);

        verify(mockFeedRepository,times(1)).findByTitle(title);
        verify(mockFeedMapper,times(1)).toResponse(feed);

        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo(title);
    }

    @Test
    void findFeedByTitle_shouldThrowException_whenFeedDoesNotExist(){
        String title = "not exist";

        when(mockFeedRepository.findByTitle(title)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{
            feedService.FindFeedByTitle(title);
        });
    }

    @Test
    void findFeedByUserId_shouldReturnAListOfFeedResponse(){
        UUID userId = UUID.randomUUID();
        List<Feed> feeds = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            Feed feed = new FeedBuilder().withTitle("title "+i).build();
            feeds.add(feed);
        });

        when(mockFeedRepository.findAllFeedsByUserId(userId)).thenReturn(feeds);
        when(mockFeedMapper.toResponse(any(Feed.class))).thenAnswer(invocation->{
            Feed f = invocation.getArgument(0);
            return new FeedResponse(
                f.getId(),
                f.getTitle(),
                f.getTopic().getTopic(), 
                f.getCreatedAt(),
                f.getUpdatedAt()
            );
        });

        List<FeedResponse> response = feedService.FindAllFeedsByUserId(userId);

        verify(mockFeedRepository,times(1)).findAllFeedsByUserId(userId);
        verify(mockFeedMapper,times(10)).toResponse(any(Feed.class));

        assertAll(()->{
            assertThat(response).isNotNull();
            response.stream().forEach(feedResponse->{
                assertThat(feedResponse).isNotNull();
            });
        });
    }

    @Test
    void deleteFeedById_shouldDeleteFeed(){
        UUID id = UUID.randomUUID();
        Feed feed = new FeedBuilder().build();

        when(mockFeedRepository.findById(id)).thenReturn(Optional.of(feed));
        
        feedService.DeleteFeedById(id);

        verify(mockFeedRepository,times(1)).findById(id);
        verify(mockFeedRepository,times(1)).delete(any(Feed.class));
    }

    @Test
    void deleteFeedById_shouldThrowException_whenFeedDoesNotExists(){
        UUID id = UUID.randomUUID();
        
        when(mockFeedRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{
            feedService.DeleteFeedById(id);
        });

        verify(mockFeedRepository,times(0)).delete(any(Feed.class));
    }
}
