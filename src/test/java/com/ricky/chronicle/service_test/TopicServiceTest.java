package com.ricky.chronicle.service_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.ricky.chronicle.dto.topic.TopicResponse;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.map.TopicMapper;
import com.ricky.chronicle.repository.TopicRepository;
import com.ricky.chronicle.service.TopicService;
import com.ricky.chronicle.util.TopicBuilder;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {
    @Mock
    TopicRepository mockTopicRepository;

    @Mock
    TopicMapper mockTopicMapper;

    @InjectMocks
    TopicService topicService;

    @Test
    void createTopic_shouldCreateTopic(){
        String topicString = "topic";
        Topic topic = new TopicBuilder().withTopic(topicString).build();
        
        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.empty());
        when(mockTopicRepository.save(any(Topic.class))).thenReturn(topic);
        when(mockTopicMapper.toResponse(topic)).thenReturn(new TopicResponse(UUID.randomUUID(), topicString));

        TopicResponse response = topicService.createTopic(topicString);

        verify(mockTopicRepository,times(1)).save(argThat(
            t->
            t.getTopic().equals(topicString)
        ));

        assertThat(response).isNotNull();
        assertThat(response.topic()).isEqualTo(topicString);
    }

    @Test
    void createTopic_shouldThrowException_whenTopicAlreadyExists(){
        String topicString = "already exists";
        Topic topic = new TopicBuilder().withTopic(topicString).build();

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.of(topic));

        assertThrows(IllegalArgumentException.class, ()->{
            topicService.createTopic(topicString);
        });

        verify(mockTopicRepository,times(0)).save(any(Topic.class));
    }

    @Test
    void getAllTopics_shouldReturnAListOfTopicResponse(){
        List<Topic> topics = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            String topicString = "topic "+i;
            topics.add(new TopicBuilder().withTopic(topicString).build());
        });

        when(mockTopicRepository.findAll()).thenReturn(topics);
        when(mockTopicMapper.toResponse(any(Topic.class))).thenAnswer(invocation->{
            Topic topic = invocation.getArgument(0);
            TopicResponse topicResponse = new TopicResponse(UUID.randomUUID(), topic.getTopic());
            return topicResponse;
        });

        List<TopicResponse> response = topicService.getAllTopics();

        verify(mockTopicRepository,times(1)).findAll();
        verify(mockTopicMapper,times(10)).toResponse(any(Topic.class));

        assertAll(()->{
            assertThat(response).isNotNull();
            response.stream().forEach(topicResponse->{
                assertThat(topicResponse).isNotNull();
            });
        }); 
    }

    @Test
    void getTopicByTopic_shouldReturnTopicResponse(){
        String topicString = "topic";
        Topic topic = new TopicBuilder().withTopic(topicString).build();

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.of(topic));
        when(mockTopicMapper.toResponse(topic)).thenReturn(new TopicResponse(UUID.randomUUID(), topicString));

        TopicResponse response = topicService.getTopicByTopic(topicString);

        verify(mockTopicRepository,times(1)).findByTopic(topicString);
        verify(mockTopicMapper,times(1)).toResponse(topic);

        assertThat(response).isNotNull();
        assertThat(response.topic()).isEqualTo(topicString);
    }

    @Test
    void getTopicByTopic_shouldThrowException_whenTopicDoesNotExist(){
        String topicString = "already exists";

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{
            topicService.getTopicByTopic(topicString);
        });
    }

    @Test
    void deleteTopicByTopic_shouldHaveNoReturnValue(){
        String topicString = "topic";
        Topic topic = new TopicBuilder().withTopic(topicString).build();

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.of(topic));
        
        topicService.deleteTopicByTopic(topicString);
    }

    @Test
    void deleteTopicByTopic_shouldThrowException_whenTopicDoesNotExists(){
        String topicString = "not exists";

        when(mockTopicRepository.findByTopic(topicString)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{
            topicService.deleteTopicByTopic(topicString);
        });

        verify(mockTopicRepository,times(0)).delete(any(Topic.class));
    }
}
