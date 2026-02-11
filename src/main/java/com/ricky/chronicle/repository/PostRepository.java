package com.ricky.chronicle.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entity.Post;

import java.util.List;
import java.util.Optional;
import com.ricky.chronicle.entity.Feed;



@Repository
public interface PostRepository extends JpaRepository<Post,UUID>{
    Optional<Post> findByTitle(String title);

    Optional<Post> findByUrl(String url);

    Optional<Post> findByFeedAndUrl(Feed feed, String url);

    @Query("""
            SELECT p FROM Post p
            LEFT JOIN FETCH p.postsByUsers u
            LEFT JOIN FETCH u.user
            WHERE u.user.username = :username
            """)
    List<Post> findAllPostsByUserUsername(@Param("username")String username);
}
