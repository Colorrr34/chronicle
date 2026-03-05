package com.ricky.chronicle.service_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.dto.user.CreateUserRequest;
import com.ricky.chronicle.dto.user.UserResponse;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.exception.InvalidArgumentException;
import com.ricky.chronicle.map.UserMapper;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.service.UserService;
import com.ricky.chronicle.util.UserBuilder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository mockUserRepository;

    @Mock
    AuthService mockAuthService;

    @Mock
    UserMapper mockUserMapper;

    @InjectMocks
    UserService userService;

    @Test
    void createUser_shouldHashPasswordAndSaveUser(){
        String username = "testUser1";
        String rawPassword = "test_password123";
        String hashedPassword = "hashedPassword";
        User mockUser = new UserBuilder().withUsernameAndHashedPassword(username, hashedPassword).build();
        CreateUserRequest request = new CreateUserRequest(username, rawPassword);
        UserResponse response = new UserResponse(null, username, null, null, null);
        
        when(mockAuthService.hashPassword(rawPassword)).thenReturn(hashedPassword);
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);
        when(mockUserMapper.toEntity(request)).thenReturn(mockUser);
        when(mockUserMapper.toResponse(mockUser)).thenReturn(response);

        UserResponse userResponse =  userService.createUser(request);
        
        verify(mockAuthService,times(1)).hashPassword(rawPassword);

        verify(mockUserRepository,times(1)).save(argThat(
            u->
            u.getUsername().equals(username)
            &&u.getHashedPassword().equals(hashedPassword))
        );

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.username()).isEqualTo(username);
    }

    @Test
    void createUser_shouldThrowException_whenUsernameExists(){
        String username = "existingUser";
        String rawPassword = "password";
        CreateUserRequest request = new CreateUserRequest(username, rawPassword);

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, ()->{
            userService.createUser(request);
        });

        verify(mockUserRepository,times(0)).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowException_whenUsernameFieldIsBlank(){
        String username = "";
        String rawPassword = "raw_password123";
        CreateUserRequest request = new CreateUserRequest(username, rawPassword);

        assertThrows(InvalidArgumentException.class,()->{
            userService.createUser(request);
        });
    }

    @Test
    void getAllUsers_shouldReturnAListOfUserResponses(){
        List<User> users = new ArrayList<>();
        
        IntStream.range(0, 10).forEach(i->{
            User user = new UserBuilder().withUsername("user "+i).build();
            users.add(user);
        });

        when(mockUserRepository.findAll()).thenReturn(users);
        when(mockUserMapper.toResponse(any(User.class))).thenAnswer(invocation->{
            User u = invocation.getArgument(0);
            return new UserResponse(null, u.getUsername(), null, null, null);
        });
    }
}
