package com.ricky.chronicle.map;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ricky.chronicle.dto.post.CreatePostRequest;
import com.ricky.chronicle.dto.post.PostResponse;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.repository.FeedRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final FeedRepository feedRepository;
    public PostResponse toResponse(Post post){
        return new PostResponse(
            post.getId(),
            post.getTitle(), 
            post.getDescription(), 
            post.getUrl(), 
            post.getFeed().getTitle(), 
            post.getCreatedAt(), 
            post.getUpdatedAt(), 
            post.getPublishedAt()
        );
    }

    public Post toPost(CreatePostRequest request){
        String feedTitle = request.feedTitle();
        String title = request.title();
        String description = request.description();
        String url = request.url();
        LocalDateTime publishedAt = request.publishedAt();

        Optional<Feed> optionalFeed = feedRepository.findByTitle(feedTitle);
        if (optionalFeed.isEmpty()){
            throw new IllegalArgumentException("No such feed");
        }

        Feed feed = optionalFeed.get();
        Post post = new Post();
        post.setTitle(title);
        post.setFeed(feed);
        post.setDescription(description);
        post.setUrl(url);
        post.setPublishedAt(publishedAt);
        return post;
    }
}
