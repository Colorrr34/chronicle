package com.ricky.chronicle.util;

import java.time.LocalDateTime;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Post;

public class PostBuilder {
    private String title = "default post title";
    private String feedTitle = "default feed title";
    private String description = "default post description";
    private String url = "www.default.com";
    private LocalDateTime publishedAt = LocalDateTime.now();

    public PostBuilder withTitle(String title){
        this.title = title;
        return this;
    }

    public PostBuilder withTitleFeedUrl(String title, String feedTitle,String url){
        this.title = title;
        this.feedTitle = feedTitle;
        this.url = url;
        return this;
    }

    public Post build(){
        Feed feed = new Feed();
        feed.setTitle(this.feedTitle);
        Post post = new Post();
        post.setTitle(this.title);
        post.setFeed(feed);
        post.setDescription(this.description);
        post.setUrl(this.url);
        post.setPublishedAt(this.publishedAt);

        return post;
    }
}
