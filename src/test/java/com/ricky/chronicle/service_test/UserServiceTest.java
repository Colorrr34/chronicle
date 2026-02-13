package com.ricky.chronicle.service_test;

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
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.exception.InvalidArgumentException;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.service.UserService;
import com.ricky.chronicle.util.UserBuilder;

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
        String hashedPassword = "hashedPassword";
        User mockUser = new UserBuilder().withUsernameAndHashedPassword(username, hashedPassword).build();
        
        when(mockAuthService.hashPassword(rawPassword)).thenReturn(hashedPassword);
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);

        User user =  userService.createUser(username,rawPassword);
        
        verify(mockAuthService,times(1)).hashPassword(rawPassword);

        verify(mockUserRepository,times(1)).save(argThat(
            u->
            u.getUsername().equals(username)
            &&u.getHashedPassword().equals(hashedPassword))
        );

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    void createUser_shouldThrowException_whenUsernameExists(){
        String username = "existingUser";
        String rawPassword = "password";
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, ()->{
            userService.createUser(username,rawPassword);
        });

        verify(mockUserRepository,times(0)).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowException_whenUsernameFieldIsBlank(){
        String username = "";
        String rawPassword = "raw_password123";

        assertThrows(InvalidArgumentException.class,()->{
            userService.createUser(username,rawPassword);
        });
    }
}
