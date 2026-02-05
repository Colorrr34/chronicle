package com.ricky.chronicle.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ricky.chronicle.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
}
