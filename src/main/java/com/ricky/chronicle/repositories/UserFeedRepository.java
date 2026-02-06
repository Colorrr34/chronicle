package com.ricky.chronicle.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entities.UserFeed;

import jakarta.transaction.Transactional;

@Repository
public interface UserFeedRepository extends JpaRepository<UserFeed,UUID>{
    @Transactional
    @Modifying
    void deleteByUserIdAndFeedId(UUID userId, UUID blogId);
}
