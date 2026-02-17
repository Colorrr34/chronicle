package com.ricky.chronicle.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic,UUID>{
    Optional<Topic> findByTopic(String topic);
    
}
