package com.ricky.chronicle.util;

import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserPost;

public class UserPostBuilder {
    private User user;
    private Post post;

    public UserPostBuilder(User user, Post post){
        this.user = user;
        this.post = post;
    }

    public UserPost build(){
        UserPost userPost = new UserPost();
        userPost.setUser(this.user);
        userPost.setPost(this.post);
        return userPost;
    }
}
