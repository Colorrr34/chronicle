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
import com.ricky.chronicle.service.TopicService;


@WebMvcTest(TopicController.class)
public class TopicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TopicService mockTopicService;

    @Test
    void getAllTopics_shouldReturn200AndAListOfTopics() throws Exception{
        List<TopicResponse> response = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            Topic topic = new Topic();
            String topicString = "topic "+i;
            topic.setTopic(topicString);
            response.add(new TopicResponse(null, topicString));
        });

        when(mockTopicService.getAllTopics()).thenReturn(response);

        mockMvc.perform(get("/api/topics"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    void postTopic_shouldReturn200AndATopic() throws Exception{
        String requestBody = "{\"topic\": \"topic\"}";
        String topicString = "topic";
        TopicResponse response = new TopicResponse(null, topicString);

        when(mockTopicService.createTopic(topicString)).thenReturn(response);

        mockMvc.perform(
            post("/api/topics").contentType(MediaType.APPLICATION_JSON).content(requestBody)
        ).andExpect(status().isCreated())
        .andExpect(jsonPath("$.topic").value(topicString));
    }

    @Test
    void deleteTopic_shouldReturn204() throws Exception{
        mockMvc.perform(delete("/api/topics/{topic}","topic"))
        .andExpect(status().isNoContent());
    }
}
