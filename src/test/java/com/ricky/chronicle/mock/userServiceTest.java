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
        String rawPassword = "test_password123";
        String hashedPassword = "hashedPassword";
        User mockUser = new User();
        mockUser.setUsername("testUser1");
        mockUser.setHashedPassword(hashedPassword);
        
        when(mockAuthService.hashPassword(rawPassword)).thenReturn(hashedPassword);
        when(mockUserRepository.findByUsername("testUser1")).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenReturn(mockUser);

        User mockedServiceUser = userService.createUser("testUser1", rawPassword);
        
        verify(mockAuthService,times(1)).hashPassword(rawPassword);

        verify(mockUserRepository,times(1)).save(argThat(
            user->
            user.getUsername().equals("testUser1")
            &&user.getHashedPassword().equals(hashedPassword))
        );
            
        assertThat(mockedServiceUser).isNotNull();
        assertThat(mockedServiceUser.getHashedPassword()).isEqualTo(hashedPassword);
    }

    @Test
    void createUser_shouldThrowException_whenUsernameExists(){
        when(mockUserRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, ()->{
            userService.createUser("existingUser", "password");
        });

        verify(mockUserRepository,times(0)).save(any(User.class));
    }
}
