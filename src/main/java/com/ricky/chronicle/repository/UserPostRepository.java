package com.ricky.chronicle.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entitie.UserPost;

@Repository
public interface UserPostRepository extends JpaRepository<UserPost,UUID>{
    
}
