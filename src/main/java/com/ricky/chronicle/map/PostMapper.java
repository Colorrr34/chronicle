package com.ricky.chronicle.map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ricky.chronicle.dto.post.CreatePostRequest;
import com.ricky.chronicle.dto.post.CreatePostResponse;
import com.ricky.chronicle.dto.post.PostResponse;
import com.ricky.chronicle.dto.post.PostSummary;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.UserPost;

@Mapper(componentModel = "spring",uses = {FeedMapper.class})
public interface PostMapper {
    @Mapping(source = "feed",target = "feedSummary")
    PostResponse toResponse(Post post);

    @Mapping(source = "feed.title",target="feedTitle")
    PostSummary toSummary(Post post);

    @Mapping(source = "post", target = "postResponse")
    @Mapping(source = "userPost.id", target = "userPostId")
    CreatePostResponse toCreatePostResponse(Post post, UserPost userPost, String message);

    @Mapping(target="id",ignore = true)
    @Mapping(target="feed",ignore = true)
    @Mapping(target = "createdAt", ignore =true)
    @Mapping(target="updatedAt",ignore = true)
    @Mapping(target = "postsByUsers", ignore = true)
    Post toEntity(CreatePostRequest request);
}
