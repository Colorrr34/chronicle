package com.ricky.chronicle.util;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserFeed;

public class UserFeedBuilder {
    private User user;
    private Feed feed;

    public UserFeedBuilder(User user, Feed feed){
        this.user = user;
        this.feed = feed;
    }

    public UserFeed build(){
        UserFeed userFeed = new UserFeed();
        userFeed.setUser(this.user);
        userFeed.setFeed(this.feed);
        return userFeed;
    }

}
