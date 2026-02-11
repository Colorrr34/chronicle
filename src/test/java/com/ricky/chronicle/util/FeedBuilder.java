package com.ricky.chronicle.util;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Topic;

public class FeedBuilder {
    private String title = "default title";
    private String topic = "default_topic";

    public FeedBuilder withTitle(String title){
        this.title = title;
        return this;
    }

    public FeedBuilder withTopic(String topic){
        this.topic = topic;
        return this;
    }

    public FeedBuilder withTitleAndTopic(String title, String topic){
        this.title = title;
        this.topic = topic;
        return this;
    }

    public Feed build(){
        Feed feed = new Feed();
        Topic topic = new Topic();
        topic.setTopic(this.topic);
        feed.setTitle(this.title);
        feed.setTopic(topic);

        return feed;
    }
}
