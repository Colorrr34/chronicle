package com.ricky.chronicle.mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserPost;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.PostRepository;
import com.ricky.chronicle.repository.UserPostRepository;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.service.PostService;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    PostRepository mockPostRepository;

    @Mock
    FeedRepository mockFeedRepository;

    @Mock
    UserRepository mockUserRepository;

    @Mock
    UserPostRepository mockUserPostRepository;

    @InjectMocks
    PostService postService;

    @Test
    void createPost_shouldCreatePostAndUserFeed_whenFeedAndUserExists(){
        String username = "username";
        String feedTitle = "feed";
        String postTitle = "post_title";
        String postDescription = "test description";
        String postUrl = "www.post.com";
        LocalDateTime publishedAt = LocalDateTime.now();
        User mockUser = new User();
        mockUser.setUsername(username);
        Feed mockFeed = new Feed();
        mockFeed.setTitle(feedTitle);
        Post mockPost = new Post();
        mockPost.setTitle(postTitle);
        mockPost.setDescription(postDescription);
        mockPost.setUrl(postUrl);
        mockPost.setFeed(mockFeed);
        UserPost mockUserPost = new UserPost();
        mockUserPost.setUser(mockUser);
        mockUserPost.setPost(mockPost);

        when(mockUserRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.of(mockFeed));
        when(mockPostRepository.save(any(Post.class))).thenReturn(mockPost);
        when(mockUserPostRepository.save(any(UserPost.class))).thenReturn(mockUserPost);

        Post mockServicePost = postService.createPost(username, feedTitle, postTitle, postDescription, postUrl, publishedAt);

        verify(mockUserRepository, times(1)).findByUsername(username);
        verify(mockFeedRepository,times(1)).findByTitle(feedTitle);
        verify(mockPostRepository,times(1)).save(argThat(
            post->
            post.getDescription().equals(postDescription)&&
            post.getTitle().equals(postTitle)&&
            post.getUrl().equals(postUrl)&&
            post.getFeed().getTitle().equals(feedTitle)
        ));
        verify(mockUserPostRepository,times(1)).save(argThat(
            up->
            up.getUser().getUsername().equals(username)&&
            up.getPost().getUrl().equals(postUrl)
        ));

        assertThat(mockServicePost).isNotNull();
        assertThat(mockServicePost.getUrl()).isEqualTo(postUrl);
    }
}
