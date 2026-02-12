package com.ricky.chronicle.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final UserPostRepository userPostRepository;

    public PostResponse createPost(CreatePostRequest request){
        UUID userId = request.userId();
        String feedTitle = request.feedTitle();
        String title = request.title();
        String description = request.description();
        String url = request.url();
        LocalDateTime publishedAt = request.publishedAt();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new IllegalArgumentException("No such user");
        }
        User user = optionalUser.get();
        Optional<Feed> optionalFeed = feedRepository.findByTitle(feedTitle);
        if (optionalFeed.isEmpty()){
            throw new IllegalArgumentException("No such user");
        }
        Feed feed = optionalFeed.get();
        Post post = new Post();
        Post dbPost;
        Optional<Post> optionalPost = postRepository.findByFeedAndUrl(feed, url);
        if(optionalPost.isEmpty()){
            post.setTitle(title);
            post.setFeed(feed);
            post.setDescription(description);
            post.setUrl(url);
            post.setPublishedAt(publishedAt);
            dbPost = postRepository.save(post);
        }else{
            dbPost = optionalPost.get();
        } 
        
        UserPost userPost = new UserPost();
        userPost.setUser(user);
        userPost.setPost(dbPost);

        UserPost savedUserPost = userPostRepository.save(userPost);
        return new PostResponse(
            dbPost.getId(), 
            savedUserPost.getUser().getId(), 
            dbPost.getFeed().getTitle(), 
            dbPost.getTitle(), 
            dbPost.getDescription(), 
            dbPost.getUrl(), 
            dbPost.getCreatedAt(), 
            dbPost.getUpdatedAt(),
            dbPost.getPublishedAt(),
            savedUserPost.getId());
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post getPostById(UUID id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()){
            throw new NoSuchElementException("Post not found");
        }
        return optionalPost.get();
    }

    public List<Post> getPostsByUserId(UUID userId){
        return postRepository.findAllPostsByUserId(userId);
    }
}
