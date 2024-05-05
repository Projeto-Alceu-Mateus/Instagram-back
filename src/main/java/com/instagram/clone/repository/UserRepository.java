package com.instagram.clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.instagram.clone.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameStartingWithIgnoreCase(String username);
}