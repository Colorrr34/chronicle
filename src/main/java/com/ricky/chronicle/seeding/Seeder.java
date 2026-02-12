package com.ricky.chronicle.seeding;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.Topic;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserFeed;
import com.ricky.chronicle.entity.UserPost;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.PostRepository;
import com.ricky.chronicle.repository.TopicRepository;
import com.ricky.chronicle.repository.UserFeedRepository;
import com.ricky.chronicle.repository.UserPostRepository;
import com.ricky.chronicle.repository.UserRepository;
import com.ricky.chronicle.seeding.seedingMap.FeedMap;
import com.ricky.chronicle.seeding.seedingMap.PostMap;
import com.ricky.chronicle.seeding.seedingMap.TopicMap;
import com.ricky.chronicle.seeding.seedingMap.UserMap;

import lombok.RequiredArgsConstructor;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final FeedRepository feedRepository;
    private final PostRepository postRepository;
    private final UserFeedRepository userFeedRepository;
    private final UserPostRepository userPostRepository;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run(String... args){
        System.out.println("Seeding production data...");
        seedDataFromJson();
    }

    private void seedDataFromJson(){
        String baseUrl = "src/main/java/com/ricky/chronicle/seed_data/";
        List<UserMap> usersJson = new ArrayList<>();
        List<User> savedUsers = new ArrayList<>();
        List<TopicMap> topicsJson = new ArrayList<>();
        List<FeedMap> feedsJson = new ArrayList<>();
        List<Feed> savedFeeds = new ArrayList<>();
        List<PostMap> postsJson = new ArrayList<>();
        List<Post> savedPosts = new ArrayList<>();

        try {
            usersJson = reader(baseUrl+"users.json", new TypeReference<List<UserMap>>(){});
            topicsJson = reader(baseUrl+"topics.json", new TypeReference<List<TopicMap>>(){});
            feedsJson = reader(baseUrl+"feeds.json", new TypeReference<List<FeedMap>>(){});
            postsJson = reader(baseUrl+"posts.json", new TypeReference<List<PostMap>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (UserMap userJson: usersJson){
            String hashedPassword = authService.hashPassword(userJson.getRawPassword());
            User user = new User();
            user.setUsername(userJson.getUsername());
            user.setHashedPassword(hashedPassword);

            User savedUser = userRepository.save(user);
            savedUsers.add(savedUser);
        }
        for (TopicMap topicJson:topicsJson){
            Topic topic = new Topic();
            topic.setTopic(topicJson.getTopic());
            
            topicRepository.save(topic);
        }
        for (FeedMap feedJson: feedsJson){
            Feed feed = new Feed();
            feed.setTitle(feedJson.getTitle());
            feed.setTopic(topicRepository.findByTopic(feedJson.getTopicRef()).get());

            Feed savedFeed = feedRepository.save(feed);
            savedFeeds.add(savedFeed);
        }
        for (PostMap postJson: postsJson){
            Post post = new Post();
            post.setTitle(postJson.getTitle());
            post.setDescription(postJson.getDescription());
            if (feedRepository.findByTitle(postJson.getFeedRef()).isEmpty()){
                System.out.println("FEED REFERENCE>>>>>>>>>>>>>:" +postJson.getFeedRef());
            }
            post.setFeed(feedRepository.findByTitle(postJson.getFeedRef()).get());
            post.setPublishedAt(LocalDateTime.now());
            post.setUrl(postJson.getUrl());

            Post savedPost = postRepository.save(post);
            savedPosts.add(savedPost);
        }
        for (int i=0;i<savedFeeds.size();i++){
            UserFeed userFeed = new UserFeed();
            userFeed.setUser(savedUsers.get(i%savedUsers.size()));
            userFeed.setFeed(savedFeeds.get(i));

            userFeedRepository.save(userFeed);
        }
        for (int i=0;i<savedPosts.size();i++){
            UserPost userPost = new UserPost();
            userPost.setUser(savedUsers.get(i%savedUsers.size()));
            userPost.setPost(savedPosts.get(i));

            userPostRepository.save(userPost);
        }
    }

    private <T> T reader(String fileLocation, TypeReference<T> typeReference) throws Exception { 
        return objectMapper.readValue(new File(fileLocation), typeReference);
    }

    
}
