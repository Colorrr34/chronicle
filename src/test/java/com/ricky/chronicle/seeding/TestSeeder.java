package com.ricky.chronicle.seeding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.entity.*;
import com.ricky.chronicle.repository.*;

import net.datafaker.Faker;

@Component
public class TestSeeder {
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

    protected Faker faker = new Faker();
    protected List<User> seededUsers = new ArrayList<>();
    protected List<Feed> seededFeeds = new ArrayList<>();
    protected List<Topic> seededTopics = new ArrayList<>();
    protected List<Post> seededPosts = new ArrayList<>();
    protected List<UserFeed> seededUserFeeds = new ArrayList<>();
    protected List<UserPost> seededUserPosts = new ArrayList<>();

    public void seedTestData(){
        seededUsers.clear();
        seededTopics.clear();
        seededFeeds.clear();
        seededPosts.clear();
        seededUserFeeds.clear();
        seededUserPosts.clear();
        
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
            System.out.println("Topic:"+seededTopic.getTopic()+", ID:"+seededTopic.getId());

            seededTopics.add(seededTopic);
        }

        for (Topic t : seededTopics){
            System.out.println(t.getTopic()+" ID: "+t.getId());
        }
        IntStream.range(0,20).forEach(i->{
            Feed feed = new Feed();
            feed.setTitle(faker.lorem().sentence(3));
            feed.setTopic(seededTopics.get(ThreadLocalRandom.current().nextInt(seededTopics.size())));
            Feed seededFeed = feedRepository.save(feed);
            seededFeeds.add(seededFeed);
        });

        IntStream.range(0,100).forEach(i->{
            Post post = new Post();
            post.setTitle(faker.lorem().sentence(10));
            post.setDescription(faker.lorem().sentence());
            post.setFeed(seededFeeds.get(i%seededFeeds.size()));
            Post seededPost = postRepository.save(post);
            seededPosts.add(seededPost);
        });

        final int USERFEED_PAIRS = 100;
        
        Set<String> uniqueUserFeedPairings = new HashSet<>();

        while(uniqueUserFeedPairings.size()< USERFEED_PAIRS){
            User randomUser = seededUsers.get(ThreadLocalRandom.current().nextInt(seededUsers.size()));
            Feed randomFeed = seededFeeds.get(ThreadLocalRandom.current().nextInt(seededUsers.size()));

            String pairKey = randomUser.getId()+":"+randomFeed.getId();

            uniqueUserFeedPairings.add(pairKey);
        }

        for (String pair : uniqueUserFeedPairings){
            UUID userId = UUID.fromString(pair.split(":")[0]);
            UUID feedId = UUID.fromString(pair.split(":")[1]);

            User user = userRepository.findById(userId).orElseThrow();
            Feed feed = feedRepository.findById(feedId).orElseThrow();

            UserFeed userFeed = new UserFeed();
            userFeed.setUser(user);
            userFeed.setFeed(feed);
            userFeedRepository.save(userFeed);
        }

        final int USERPOST_PAIRS = 200; 
        
        Set<String> uniqueUserPostPairings = new HashSet<>();

        while (uniqueUserPostPairings.size() < USERPOST_PAIRS) {
            User randomUser = seededUsers.get(ThreadLocalRandom.current().nextInt(seededUsers.size()));
            Post randomPost = seededPosts.get(ThreadLocalRandom.current().nextInt(seededPosts.size())); 

            String pairKey = randomUser.getId() + ":" + randomPost.getId();

            uniqueUserPostPairings.add(pairKey);
        }

        for (String pair : uniqueUserPostPairings){
            UUID userId = UUID.fromString(pair.split(":")[0]);
            UUID postId = UUID.fromString(pair.split(":")[1]);

            User user = userRepository.findById(userId).orElseThrow(); 
            Post post = postRepository.findById(postId).orElseThrow();

            UserPost userPost = new UserPost();
            userPost.setUser(user);
            userPost.setPost(post);
            
            userPostRepository.save(userPost);
        }
    }
}
