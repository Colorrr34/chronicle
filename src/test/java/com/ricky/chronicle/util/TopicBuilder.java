package com.ricky.chronicle.util;

import com.ricky.chronicle.entity.Topic;

public class TopicBuilder {
    private String topic = "default_topic";

    public TopicBuilder withTopic(String topic){
        this.topic = topic;
        return this;
    }

    public Topic build(){
        Topic topic = new Topic();
        topic.setTopic(this.topic);
        return topic;
    }
}
