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

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository mockUserRepository;

    @Mock
    AuthService mockAuthService;

    @InjectMocks
    UserService userService;

    @Test
    void createUser_shouldHashPasswordAndSaveUser(){
        String username = "testUser1";
        String rawPassword = "test_password123";
        CreateUserRequest request = new CreateUserRequest(username,rawPassword);
        String hashedPassword = "hashedPassword";
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setHashedPassword(hashedPassword);
        
        when(mockAuthService.hashPassword(rawPassword)).thenReturn(hashedPassword);
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse response =  userService.createUser(request);
        
        verify(mockAuthService,times(1)).hashPassword(rawPassword);

        verify(mockUserRepository,times(1)).save(argThat(
            user->
            user.getUsername().equals(username)
            &&user.getHashedPassword().equals(hashedPassword))
        );

        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(username);
    }

    @Test
    void createUser_shouldThrowException_whenUsernameExists(){
        String username = "existingUser";
        CreateUserRequest request = new CreateUserRequest(username, "password");
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, ()->{
            userService.createUser(request);
        });

        verify(mockUserRepository,times(0)).save(any(User.class));
    }
}
