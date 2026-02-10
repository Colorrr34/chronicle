package com.ricky.chronicle.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

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

    public Post createPost(
        String username,
        String feedTitle,
        String postTitle,
        String description,
        String url,
        LocalDateTime publishedAt
    ){
        User user = userRepository.findByUsername(username).get();
        Feed feed = feedRepository.findByTitle(feedTitle).get();
        Post post = new Post();
        post.setTitle(postTitle);
        post.setFeed(feed);
        post.setDescription(description);
        post.setUrl(url);
        post.setPublishedAt(publishedAt);
        Post savedPost = postRepository.save(post);
        UserPost userPost = new UserPost();
        userPost.setUser(user);
        userPost.setPost(savedPost);

        userPostRepository.save(userPost);
        return savedPost;
    }
}
