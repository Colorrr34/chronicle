package com.ricky.chronicle.map;

import org.springframework.stereotype.Component;

import com.ricky.chronicle.dto.post.PostResponse;
import com.ricky.chronicle.entity.Post;

@Component
public class PostMapper {
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
}
