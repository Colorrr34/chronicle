package com.ricky.chronicle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.entity.*;
import com.ricky.chronicle.repository.*;


import net.datafaker.Faker;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public abstract class AbstractIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserFeedRepository userFeedRepository;

    @Autowired
    private UserPostRepository userPostRepository;

    @Autowired
    private AuthService auth;

    @BeforeEach
    void setUp(){
        seedData();      
    }

    protected Faker faker = new Faker();
    protected List<User> seededUsers = new ArrayList<>();
    protected List<Feed> seededFeeds = new ArrayList<>();
    protected List<Topic> seededTopics = new ArrayList<>();
    protected List<Post> seededPosts = new ArrayList<>();
    protected List<UserFeed> seededUserFeeds = new ArrayList<>();
    protected List<UserPost> seededUserPosts = new ArrayList<>();

    protected void seedData(){
        
        IntStream.range(0,10).forEach(i->{
            User user = new User();
            String hashedPassword = auth.hashPassword(faker.credentials().password());
            user.setUsername(faker.credentials().username());
            user.setHashedPassword(hashedPassword);
            User seededUser = userRepository.save(user);
            seededUsers.add(seededUser);
        });
            
        List<String> topics = List.of("C","typescript","golang","rust","python");
        for (String topicString : topics){
            Topic topic = new Topic();
            topic.setTopic(topicString);
            Topic seededTopic = topicRepository.save(topic);
            seededTopics.add(seededTopic);
        }

        IntStream.range(0,20).forEach(i->{
            Feed feed = new Feed();
            feed.setTitle(faker.lorem().sentence(3));
            feed.setTopic(seededTopics.get(i%5));
            Feed seededFeed = feedRepository.save(feed);
            seededFeeds.add(seededFeed);
        });

        IntStream.range(0,100).forEach(i->{
            Post post = new Post();
            post.setTitle(faker.lorem().sentence(10));
            post.setDescription(faker.lorem().sentence());
            post.setFeed(seededFeeds.get(i%20));
            Post seededPost = postRepository.save(post);
            seededPosts.add(seededPost);
        });

        IntStream.range(0,50).forEach(i->{
            UserFeed userFeed = new UserFeed();
            userFeed.setUser(seededUsers.get(i%10));
            userFeed.setFeed(seededFeeds.get(i%20));
            UserFeed seededFeed = userFeedRepository.save(userFeed);
            seededUserFeeds.add(seededFeed);
        });

        IntStream.range(0,300).forEach(i->{
            UserPost userPost = new UserPost();
            userPost.setUser(seededUsers.get(i%10));
            userPost.setPost(seededPosts.get(i%100));
            UserPost seededPost = userPostRepository.save(userPost);
            seededUserPosts.add(seededPost);
        });
    }
}
