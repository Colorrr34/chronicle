package com.ricky.chronicle.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricky.chronicle.entity.Topic;

public interface TopicRepository extends JpaRepository<Topic,UUID>{
    Optional<Topic> findByTopic(String topic);
    
}
