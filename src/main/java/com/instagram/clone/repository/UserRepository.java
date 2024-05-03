package com.instagram.clone.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.instagram.clone.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}