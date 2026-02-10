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

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserFeed;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.UserFeedRepository;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.service.UserFeedService;

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
        String username = "username";
        String hashedPassword = "hashed_password123";
        String feedTitle = "feed title";
        String feedTopic = "topic";
        User mockUser = new User();      
        mockUser.setUsername(username);
        mockUser.setHashedPassword(hashedPassword);
        Topic mockTopic = new Topic();
        mockTopic.setTopic(feedTopic);
        Feed mockFeed = new Feed();
        mockFeed.setTitle(feedTitle);
        mockFeed.setTopic(mockTopic);
        UserFeed mockUserFeed = new UserFeed();
        mockUserFeed.setUser(mockUser);
        mockUserFeed.setFeed(mockFeed);
        
        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.of(mockFeed));
        when(mockUserFeedRepository.save(any(UserFeed.class))).thenReturn(mockUserFeed);

        UserFeed mockServiceUserFeed = userFeedService.createUserFeed(username, feedTitle);

        verify(mockUserRepository,times(1)).findByUsername(username);
        verify(mockFeedRepository,times(1)).findByTitle(feedTitle);
        verify(mockUserFeedRepository,times(1)).save(argThat(
            uf->
            uf.getUser().getUsername().equals(username)&&
            uf.getFeed().getTitle().equals(feedTitle)
        ));

        assertThat(mockServiceUserFeed).isNotNull();
        assertThat(mockServiceUserFeed.getUser().getUsername()).isEqualTo(username);
        assertThat(mockServiceUserFeed.getFeed().getTitle()).isEqualTo(feedTitle);
    }

    @Test
    void createUserFeed_shouldThrowException_whenUserNotExists(){
        String username = "existing_user";
        String feedTitle = "feed title";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->{
            userFeedService.createUserFeed(username, feedTitle);
        });

        verify(mockUserFeedRepository,times(0)).save(any(UserFeed.class));
    }

    @Test
    void createUserFeed_shouldThrowException_whenFeedNotExists(){
        String username = "username";
        String feedTitle = "already exists";

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(new User()));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->{
            userFeedService.createUserFeed(username, feedTitle);
        });

        verify(mockUserFeedRepository,times(0)).save(any(UserFeed.class));
    }
}
