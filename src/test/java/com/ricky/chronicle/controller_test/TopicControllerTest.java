package com.ricky.chronicle.controller_test;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ricky.chronicle.controller.TopicController;
import com.ricky.chronicle.dto.topic.TopicResponse;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.map.TopicMapper;
import com.ricky.chronicle.service.TopicService;
import com.ricky.chronicle.util.TopicBuilder;

@WebMvcTest(TopicController.class)
public class TopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TopicService mockTopicService;

    @MockitoBean
    private TopicMapper mockTopicMapper;

    @Test
    void getAllTopics_shouldReturn200AndAListOfTopics() throws Exception{
        List<Topic> topics = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            Topic topic = new Topic();
            topic.setTopic("topic "+i);
            topics.add(topic);
        });

        when(mockTopicService.getAllTopics()).thenReturn(topics);

        mockMvc.perform(get("/api/topics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    void postTopic_shouldReturn200AndATopic() throws Exception{
        String requestBody = "{\"topic\": \"topic\"}";
        String topicString = "topic";
        Topic topic = new TopicBuilder().withTopic(topicString).build();

        when(mockTopicService.createTopic(topicString)).thenReturn(topic);
        when(mockTopicMapper.toResponse(topic)).thenReturn(new TopicResponse(null, topicString));

        mockMvc.perform(
            post("/api/topics").contentType(MediaType.APPLICATION_JSON).content(requestBody)
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.topic").value(topicString));
    }

    @Test
    void deleteTopic_shouldReturn200AndAMessage() throws Exception{
        when(mockTopicService.deleteTopicByTopic("topic")).thenReturn("topic deleted");

        mockMvc.perform(delete("/api/topics/{topic}","topic"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value("topic deleted"));
    }
}
