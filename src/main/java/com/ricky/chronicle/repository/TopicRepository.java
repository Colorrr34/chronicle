package com.ricky.chronicle.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ricky.chronicle.entitie.Topic;

public interface TopicRepository extends JpaRepository<Topic,UUID>{

    
}
