package com.ricky.chronicle.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entity.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed,UUID>{
    Optional<Feed> findByTitle(String title);

    @Query("""
            SELECT f FROM Feed f
            LEFT JOIN fetch f.feedsByUsers uf
            LEFT JOIN fetch uf.user
            WHERE uf.user.username = :username
            """)
    List<Feed> findAllFeedsByUserUsername(@Param("username")String username);

    @Query("""
            SELECT f FROM Feed f
            LEFT JOIN fetch f.feedsByUsers uf
            WHERE uf.user.id = :userId
            """)
    List<Feed> findAllFeedsByUserId(@Param("userId")UUID userId);
}
