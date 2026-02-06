package com.ricky.chronicle;

import java.util.ArrayList;
import java.util.List;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ricky.chronicle.auth.AuthService;
import com.ricky.chronicle.entitie.User;
import com.ricky.chronicle.repository.FeedRepository;
import com.ricky.chronicle.repository.PostRepository;
import com.ricky.chronicle.repository.TopicRepository;
import com.ricky.chronicle.repository.UserFeedRepository;
import com.ricky.chronicle.repository.UserPostRepository;
import com.ricky.chronicle.repository.UserRepository;

import net.datafaker.Faker;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractIntegrationTest {
    protected Faker faker = new Faker();

    @Autowired
    private Flyway flyway;

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
    void setUpDatabase(){
        flyway.clean();
        flyway.migrate();      
    }

    protected void seedData(){
        List<User> userList = new ArrayList<>();
        for (int i=0;i<10;i++){
            User user = new User();
            String hashedPassword = auth.hashPassword(faker.credentials().password());
            user.setUsername(faker.credentials().username());
            user.setHashedPassword(hashedPassword);
            User dbUser = userRepository.save(user);
            userList.add(dbUser);
        }
    }
}
