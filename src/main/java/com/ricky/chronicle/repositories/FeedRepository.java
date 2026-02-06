package com.ricky.chronicle.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entities.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed,UUID>{
    Optional<Feed> findByTitle(String title);

    @Query("""
            SELECT f FROM Feed f
            JOIN f.feedsByUsers u
            WHERE u.user.username = :username
            """)
    List<Feed> findAllByFeedsByUsers(@Param("username")String username);
}
