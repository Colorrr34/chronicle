package com.ricky.chronicle.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entity.Feed;
import com.ricky.chronicle.entity.User;
import com.ricky.chronicle.entity.UserFeed;

@Repository
public interface UserFeedRepository extends JpaRepository<UserFeed,UUID>{
    @Modifying
    void deleteByUserAndFeed(User user, Feed feed);
}
