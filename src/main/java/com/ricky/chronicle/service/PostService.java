package com.ricky.chronicle.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ricky.chronicle.dto.post.CreatePostRequest;
import com.ricky.chronicle.dto.post.PostResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserPost;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.PostRepository;
import com.ricky.chronicle.repository.UserPostRepository;
import com.ricky.chronicle.repository.UserRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final UserPostRepository userPostRepository;

    public PostService(
        PostRepository postRepository,
        UserRepository userRepository,
        FeedRepository feedRepository,
        UserPostRepository userPostRepository
    ){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
        this.userPostRepository = userPostRepository;
    }

    public PostResponse createPost(CreatePostRequest request){
        UUID userId = request.userId();
        String feedTitle = request.feedTitle();
        String title = request.title();
        String description = request.description();
        String url = request.url();
        LocalDateTime publishedAt = request.publishedAt();
        User user = userRepository.findById(userId).get();
        Feed feed = feedRepository.findByTitle(feedTitle).get();
        Post post = new Post();
        post.setTitle(title);
        post.setFeed(feed);
        post.setDescription(description);
        post.setUrl(url);
        post.setPublishedAt(publishedAt);
        Post savedPost = postRepository.save(post);
        UserPost userPost = new UserPost();
        userPost.setUser(user);
        userPost.setPost(savedPost);

        UserPost savedUserPost = userPostRepository.save(userPost);
        return new PostResponse(
            savedPost.getId(), 
            savedUserPost.getUser().getId(), 
            savedPost.getFeed().getTitle(), 
            savedPost.getTitle(), 
            savedPost.getDescription(), 
            savedPost.getUrl(), 
            savedPost.getCreatedAt(), 
            savedPost.getUpdatedAt(),
            savedPost.getPublishedAt(),
            savedUserPost.getId());
    }
}
