package com.ricky.chronicle.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entity.Post;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserPost;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost,UUID>{
    @Modifying
    void deleteByUserAndPost(User user, Post post);
}
