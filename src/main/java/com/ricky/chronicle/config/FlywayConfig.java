package com.ricky.chronicle.config;

import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            System.out.println("Flyway cleaning and migrating...");
            flyway.clean();
            flyway.migrate();
        };
    }
}
