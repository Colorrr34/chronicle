package com.ricky.chronicle.controller_test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*; 

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ricky.chronicle.controller.UserController;
import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.map.UserFeedMapper;
import com.ricky.chronicle.map.UserMapper;
import com.ricky.chronicle.service.UserFeedService;
import com.ricky.chronicle.service.UserPostService;
import com.ricky.chronicle.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService mockUserService;

    @MockitoBean
    private UserFeedService mockUserFeedService;

    @MockitoBean
    private UserMapper mockUserMapper;

    @MockitoBean
    private UserFeedMapper mockFeedMapper;

    @MockitoBean
    private UserPostService mockUserPostService;

    @Test
    void getAllUsers_shouldReturn200AndListOfUsers() throws Exception{
        List<UserResponse> response = new ArrayList<>();

        for (int i = 0;i<10;i++ ){
            response.add(new UserResponse(UUID.randomUUID(), "user"+i,null, null, null));
        }

        when(mockUserService.getAllUsers()).thenReturn(response);

        mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    void getUser_shouldReturn200AndUser() throws Exception{
        UUID userId = UUID.randomUUID();
        String username = "test_user";

        when(mockUserService.getUserById(userId)).thenReturn(new UserResponse(userId, username, null, null, null));
        
        mockMvc.perform(get("/api/users/{userId}",userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("test_user"));
    }

    @Test
    void postUser_shouldReturn201AndUser() throws Exception{
        String requestBody = "{\"username\": \"test_user\", \"rawPassword\": \"raw_password123\"}";
        String username = "test_user";
        String rawPassword = "raw_password123";
        String hashedPassword = "hashed_password";

        User user = new User();
        user.setUsername(username);
        user.setHashedPassword(hashedPassword);
        CreateUserRequest request = new CreateUserRequest(username, rawPassword);
        UserResponse response = new UserResponse(null, username, null, null, null);
        
        when(mockUserService.createUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value(username));

        verify(mockUserService,times(1)).createUser(request);
    }

    @Test
    void postUser_shouldReturn409_whenUsernameExists()throws Exception{
        String requestBody = "{\"username\": \"test_user\", \"rawPassword\": \"raw_password123\"}";
        String username = "test_user";
        String rawPassword = "raw_password123";
        CreateUserRequest request = new CreateUserRequest(username, rawPassword);

        when(mockUserService.createUser(request)).thenThrow(new IllegalArgumentException("username already exists"));

         mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isConflict());
    }
}
