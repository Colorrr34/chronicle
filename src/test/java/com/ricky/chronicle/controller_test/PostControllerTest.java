package com.ricky.chronicle.controller_test;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.chronicle.controller.PostController;
import com.ricky.chronicle.dto.post.CreatePostRequest;
import com.ricky.chronicle.dto.post.CreatePostResponse;
import com.ricky.chronicle.dto.post.PostResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.service.PostService;


@WebMvcTest(PostController.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService mockPostService;


    @Test
    void getAllPosts_shouldReturn200AndListofPosts()throws Exception{
        List<PostResponse> response = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            String title = "Title "+i;
            String description = "description";
            LocalDateTime publishedAt = LocalDateTime.now();
            String url = "www.example.com/"+i;
            response.add(new PostResponse(null, title, description, url, null, null, null, publishedAt));
        });

        when(mockPostService.getAllPosts()).thenReturn(response);

        mockMvc.perform(get("/api/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$",hasSize(10)));
    }

    @Test
    void getPostById_shouldReturn200AndAPost() throws Exception{
        UUID id = UUID.randomUUID();
        Post post = new Post();
        post.setTitle("Title ");
        post.setDescription("description");
        post.setFeed(new Feed());
        post.setPublishedAt(LocalDateTime.now());
        post.setUrl("www.example.com");
        PostResponse postResponse = new PostResponse(
            id, 
            post.getTitle(), 
            post.getDescription(), 
            post.getUrl(), 
            null, 
            null, 
            null, 
            post.getPublishedAt()
        );

        when(mockPostService.getPostById(id)).thenReturn(postResponse);

        mockMvc.perform(get("/api/posts/{id}",id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getPostByUserId_shouldReturn200AndAListOfPost() throws Exception{
        UUID id = UUID.randomUUID();

        List<PostResponse> response = new ArrayList<>();

        IntStream.range(0, 10).forEach(i->{
            String title = "Title "+i;
            String description = "description";
            LocalDateTime publishedAt = LocalDateTime.now();
            String url = "www.example.com/"+i;
            response.add(new PostResponse(null, title, description, url, null, null, null, publishedAt));
        });

        when(mockPostService.getPostsByUserId(id)).thenReturn(response);

        mockMvc.perform(get("/api/posts/users/{id}",id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$",hasSize(10)));
    }

    @Test
    void postPost_shouldReturn201AndCreatedPostResponseObjectWithAMessage() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        UUID userId = UUID.randomUUID();
        String message = "created new post and userPost";
        String title = "title";
        String feedTitle = "feed title";
        String description = "description";
        String url = "www.example.com";
        CreatePostRequest request = new CreatePostRequest(userId, title, feedTitle, description, url, null);
        CreatePostResponse response = new CreatePostResponse(message, null, new PostResponse(userId, feedTitle, description, url, null, null, null, null));

        when(mockPostService.createPost(request)).thenReturn(response);

        mockMvc.perform(post("/api/posts")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message").value(message));
    }
}
