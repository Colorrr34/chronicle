package com.ricky.chronicle.service_test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
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

import com.ricky.chronicle.dto.post.CreatePostRequest;
import com.ricky.chronicle.dto.post.CreatePostResponse;
import com.ricky.chronicle.dto.post.PostResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserPost;
import com.ricky.chronicle.map.PostMapper;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.PostRepository;
import com.ricky.chronicle.repository.UserPostRepository;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.service.PostService;
import com.ricky.chronicle.util.FeedBuilder;
import com.ricky.chronicle.util.PostBuilder;
import com.ricky.chronicle.util.UserBuilder;
import com.ricky.chronicle.util.UserPostBuilder;

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

    @Mock
    PostMapper mockPostMapper;

    @InjectMocks
    PostService postService;

    @Test
    void createPost_shouldCreatePostAndUserFeed_whenFeedAndUserExists(){
        UUID userId = UUID.randomUUID();
        String username = "username";
        String feedTitle = "feed";
        String postTitle = "post_title";
        String postDescription = "post description";
        String postUrl = "www.post.com";
        LocalDateTime publishedAt = LocalDateTime.now();
        String message= "created new post and userPost";
        User mockUser = new UserBuilder().withUsername(username).build();
        Feed mockFeed = new FeedBuilder().withTitle(feedTitle).build();
        Post mockPost = new PostBuilder().withTitleFeedUrl(postTitle, feedTitle, postUrl).build();
        UserPost mockUserPost = new UserPostBuilder(mockUser, mockPost).build();
        CreatePostRequest request = new CreatePostRequest(userId, feedTitle, postTitle, postDescription, postUrl, publishedAt);
        PostResponse postResponse = new PostResponse(userId, postTitle, postDescription, postUrl, null, null,null, publishedAt);
        CreatePostResponse createPostResponse = new CreatePostResponse(message, null, postResponse);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.of(mockFeed));
        when(mockPostRepository.findByFeedAndUrl(mockFeed, postUrl)).thenReturn(Optional.empty());
        when(mockPostRepository.save(any(Post.class))).thenReturn(mockPost);
        when(mockUserPostRepository.save(any(UserPost.class))).thenReturn(mockUserPost);
        when(mockPostMapper.toEntity(request)).thenReturn(mockPost);
        when(mockPostMapper.toCreatePostResponse(mockPost, mockUserPost, message)).thenReturn(createPostResponse);

        CreatePostResponse serviceResponse = postService.createPost(request);

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockFeedRepository,times(1)).findByTitle(feedTitle);
        verify(mockPostRepository,times(1)).findByFeedAndUrl(
            eq(mockFeed),eq(postUrl)
        );
        verify(mockPostRepository,times(1)).save(any(Post.class));
        verify(mockUserPostRepository,times(1)).save(argThat(
            up->
            up.getUser().getUsername().equals(username)&&
            up.getPost().getUrl().equals(postUrl)
        ));

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.postResponse().url()).isEqualTo(postUrl);
        assertThat(serviceResponse.message()).isEqualTo(message);
    }

    @Test
    void createPost_shouldCreateOnlyUserPost_whenPostExists(){
        UUID userId = UUID.randomUUID();
        String username = "username";
        String feedTitle = "exist feed";
        String postTitle = "post_title";
        String postDescription = "post description";
        String postUrl = "www.exist.com";
        LocalDateTime publishedAt = LocalDateTime.now();
        String message = "created only UserPost as post already exists";

        User mockUser = new UserBuilder().withUsername(username).build();
        Feed mockFeed = new FeedBuilder().withTitle(postTitle).build();
        Post mockPost = new PostBuilder().withTitleFeedUrl(postTitle, feedTitle, postUrl).build();
        UserPost mockUserPost = new UserPostBuilder(mockUser, mockPost).build();
        CreatePostRequest request = new CreatePostRequest(userId, feedTitle, postTitle, postDescription, postUrl, publishedAt);
        PostResponse postResponse = new PostResponse(userId, postTitle, postDescription, postUrl, null, null, null, publishedAt);
        CreatePostResponse createPostResponse = new CreatePostResponse(message, null, postResponse);

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.of(mockFeed));
        when(mockPostRepository.findByFeedAndUrl(mockFeed, postUrl)).thenReturn(Optional.of(mockPost));
        when(mockUserPostRepository.save(any(UserPost.class))).thenReturn(mockUserPost);
        when(mockPostMapper.toCreatePostResponse(mockPost, mockUserPost, message)).thenReturn(createPostResponse);


        CreatePostResponse serviceResponse = postService.createPost(request);

        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockFeedRepository,times(1)).findByTitle(feedTitle);
        verify(mockPostRepository,times(1)).findByFeedAndUrl(eq(mockFeed), eq(postUrl));
        verify(mockPostRepository,times(0)).save(any(Post.class));
        verify(mockUserPostRepository,times(1)).save(argThat(
            up->
            up.getUser().getUsername().equals(username)&&
            up.getPost().getUrl().equals(postUrl)
        ));

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.postResponse().url()).isEqualTo(postUrl);
        assertThat(serviceResponse.message()).isEqualTo(message);
    }

    @Test
    void createPost_shouldThrowException_whenUserDoesNotExists(){
        UUID userId = UUID.randomUUID();

        when(mockUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->{
            postService.createPost(new CreatePostRequest(userId, null, null, null, null, null));
        });

        verify(mockPostRepository,times(0)).save(any(Post.class));
    } 

    @Test
    void createPost_shouldThrowException_whenFeedDoesNotExists(){
        UUID userId = UUID.randomUUID();
        User mockUser = new UserBuilder().build();
        String feedTitle = "not exists";

        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockFeedRepository.findByTitle(feedTitle)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,()->{
            postService.createPost(new CreatePostRequest(userId, feedTitle, feedTitle, null,null, null));
        });

        verify(mockPostRepository,times(0)).save(any(Post.class));
    }
}
