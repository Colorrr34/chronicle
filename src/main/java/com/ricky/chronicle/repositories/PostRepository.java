package com.ricky.chronicle.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entities.Feed;
import com.ricky.chronicle.entities.Post;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post,UUID>{
    Optional<Post> findByTitle(String title);

    @Query("""
            SELECT p FROM Post p
            JOIN p.postsByUsers u
            WHERE u.user.username = :username
            """)
    List<Feed> findAllByPostsByUsers(@Param("username")String username);
}
