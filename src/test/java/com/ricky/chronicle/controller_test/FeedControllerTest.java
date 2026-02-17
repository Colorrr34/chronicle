package com.ricky.chronicle.controller_test;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ricky.chronicle.controller.FeedController;
import com.ricky.chronicle.dto.feed.FeedResponse;
import com.ricky.chronicle.service.FeedService;

@WebMvcTest(FeedController.class)
public class FeedControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FeedService mockFeedService;

    @Test
    void getAllFeeds_shouldReturn200AndAListOfFeeds() throws Exception{
        List<FeedResponse> response = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            response.add(new FeedResponse(UUID.randomUUID(), "title "+i, "topic "+i, LocalDateTime.now(), LocalDateTime.now()));
        });

        when(mockFeedService.FindAllFeeds()).thenReturn(response);

        mockMvc.perform(get("/api/feeds"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$",hasSize(10)));
    }

    @Test
    void getAllFeedsByUserId_shouldReturn200AndAListOfFeedsByUser()throws Exception{
        UUID userId = UUID.randomUUID();

        List<FeedResponse> response = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            response.add(new FeedResponse(UUID.randomUUID(), "title "+i, "topic "+i, LocalDateTime.now(), LocalDateTime.now()));
        });

        when(mockFeedService.FindAllFeedsByUserId(userId)).thenReturn(response);

        mockMvc.perform(get("/api/feeds/users/{userId}",userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$",hasSize(10)));
    }

    @Test
    void postFeed_shouldReturn201AndAFeedResponse()throws Exception{
        
    }
}
