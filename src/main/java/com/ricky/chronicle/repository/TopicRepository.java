package com.ricky.chronicle.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricky.chronicle.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic,UUID>{

    
}
