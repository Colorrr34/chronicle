package com.ricky.chronicle.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entities.UserFeed;

@Repository
public interface UserFeedRepository extends JpaRepository<UserFeed,UUID>{
 
}
