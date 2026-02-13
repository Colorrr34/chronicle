package com.ricky.chronicle.service_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserFeed;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.UserFeedRepository;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.service.UserFeedService;
import com.ricky.chronicle.util.FeedBuilder;
import com.ricky.chronicle.util.UserBuilder;
import com.ricky.chronicle.util.UserFeedBuilder;

@ExtendWith(MockitoExtension.class)
public class UserFeedServiceTest {
    @Mock
    UserFeedRepository mockUserFeedRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    FeedRepository mockFeedRepository;

    @InjectMocks
    UserFeedService userFeedService;

    @Test
    void createUserFeed_shouldCreateUserFeed(){
        UUID userId = UUID.randomUUID();
        String feedTitle = "feed title";
        User mockUser = new UserBuilder().build(); 
        Feed mockFeed = new FeedBuilder().withTitle(feedTitle).build();
        UserFeed mockUserFeed = new UserFeedBuilder(mockUser, mockFeed).build();
        
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.of(mockFeed));
        when(mockUserFeedRepository.save(any(UserFeed.class))).thenReturn(mockUserFeed);

        UserFeed userFeed = userFeedService.createUserFeed(userId, feedTitle);

        verify(mockUserRepository,times(1)).findById(userId);
        verify(mockFeedRepository,times(1)).findByTitle(feedTitle);
        verify(mockUserFeedRepository,times(1)).save(argThat(
            uf->
            uf.getFeed().getTitle().equals(feedTitle)
        ));

        assertThat(userFeed).isNotNull();
    }

    @Test
    void createUserFeed_shouldThrowException_whenUserNotExists(){
        UUID userId = UUID.randomUUID();
        String feedTitle = "feed title";

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->{
            userFeedService.createUserFeed(userId,feedTitle);
        });

        verify(mockUserFeedRepository,times(0)).save(any(UserFeed.class));
    }

    @Test
    void createUserFeed_shouldThrowException_whenFeedNotExists(){
        UUID userId = UUID.randomUUID();
        String feedTitle = "already exists";

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->{
            userFeedService.createUserFeed(userId,feedTitle);
        });

        verify(mockUserFeedRepository,times(0)).save(any(UserFeed.class));
    }
}
