package com.ricky.chronicle;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ricky.chronicle.seeding.TestSeeder;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
public class IntegrationTest {
    @Autowired
    private Flyway flyway;

    @Autowired
    private TestSeeder testSeeder;

    @BeforeAll
    public void setUp(){
        System.out.println("Step 1: Flyway migration");
        flyway.clean();
        flyway.migrate();

        System.out.println("Step 2: Seeding");
        testSeeder.seedTestData();
    }
}
