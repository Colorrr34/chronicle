package com.ricky.chronicle.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final UserPostRepository userPostRepository;
    private final PostMapper postMapper;

    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request){
        UUID userId = request.userId();
        String feedTitle = request.feedTitle();
        String url = request.url();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()){
            throw new IllegalArgumentException("No such user");
        }
        User user = optionalUser.get();
        Optional<Feed> optionalFeed = feedRepository.findByTitle(feedTitle);
        if (optionalFeed.isEmpty()){
            throw new IllegalArgumentException("No such feed");
        }
        Feed feed = optionalFeed.get();
        Post savedPost;
        Optional<Post> optionalPost = postRepository.findByFeedAndUrl(feed, url);
        String message;
        if(optionalPost.isEmpty()){
            Post post = postMapper.toEntity(request);
            post.setFeed(feed);
            savedPost = postRepository.save(post);

            message = "created new post and userPost";
        }else{
            savedPost = optionalPost.get();
            message = "created only UserPost as post already exists";
        } 
        
        UserPost userPost = new UserPost();
        userPost.setUser(user);
        userPost.setPost(savedPost);

        UserPost savedUserPost = userPostRepository.save(userPost);
        return postMapper.toCreatePostResponse(savedPost, savedUserPost, message);
    }

    public List<PostResponse> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        List<PostResponse> response = posts.stream().map(post->postMapper.toResponse(post)).toList();
        return response;
    }

    public PostResponse getPostById(UUID id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()){
            throw new NoSuchElementException("Post not found");
        }
        Post post = optionalPost.get();
        return postMapper.toResponse(post);
    }

    public List<PostResponse> getPostsByUserId(UUID userId){
        List<Post> posts = postRepository.findAllPostsByUserId(userId);
        List<PostResponse> response = posts.stream().map(post->postMapper.toResponse(post)).toList();

        return response;
    }

    @Transactional
    public String deletePostById(UUID postId){
        Optional<Post> optionalPost = postRepository.findById(postId);
        if(optionalPost.isEmpty()){
            throw new NoSuchElementException("post not found");
        }

        postRepository.delete(optionalPost.get());
        return "post deleted";
    }
}
