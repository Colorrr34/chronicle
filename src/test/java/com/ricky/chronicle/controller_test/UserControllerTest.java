package com.ricky.chronicle.controller_test;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*; 

import java.time.LocalDateTime;
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
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService mockUserService;

    @Test
    void getAllUsers_shouldReturn200AndListOfUsers() throws Exception{
        List<UserResponse> responseList = new ArrayList<>();

        for (int i = 0;i<10;i++ ){
            responseList.add(new UserResponse(
            UUID.randomUUID(), 
            "user_name_"+i, 
            LocalDateTime.now(), 
            LocalDateTime.now(), 
            LocalDateTime.now()));
        }

        when(mockUserService.getAllUsers()).thenReturn(responseList);

        mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(10)));
    }

    @Test
    void getUser_shouldReturn200AndUser() throws Exception{
        UUID userId = UUID.randomUUID();
        UserResponse userResponse = new UserResponse(
            userId, 
            "test_user", 
            LocalDateTime.now(), 
            LocalDateTime.now(), 
            LocalDateTime.now()
        );

        when(mockUserService.getUserById(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/users/{userId}",userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.username").value("test_user"));
    }

    @Test
    void postUser_shouldReturn201AndUser() throws Exception{
        String requestBody = "{\"username\": \"test_user\", \"rawPassword\": \"raw_password123\"}";
        String username = "test_user";
        String rawPassword = "raw_password123";

        UserResponse userResponse = new UserResponse(
            UUID.randomUUID(), 
            username, 
            LocalDateTime.now(), 
            LocalDateTime.now(), 
            LocalDateTime.now()
        );
        
        when(mockUserService.createUser(argThat(
            request ->
            request.username().equals(username)&&
            request.rawPassword().equals(rawPassword)
        ))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value(username));

        verify(mockUserService,times(1)).createUser(argThat(
            cur->
            cur.username().equals(username)&&
            cur.rawPassword().equals(rawPassword)
        ));
    }

    @Test
    void postUser_shouldReturn409_whenUsernameExists()throws Exception{
        String requestBody = "{\"username\": \"test_user\", \"rawPassword\": \"raw_password123\"}";
        String username = "test_user";
        String rawPassword = "raw_password123";

        when(mockUserService.createUser(argThat(
            request ->
            request.username().equals(username)&&
            request.rawPassword().equals(rawPassword)
        ))).thenThrow(new IllegalArgumentException("username already exists"));

         mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isConflict());
    }
}
