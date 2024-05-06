package com.instagram.clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.instagram.clone.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    List<User> findByUsernameStartingWithIgnoreCase(String username);

    @Query("SELECT COUNT(f) FROM User u JOIN u.followers f WHERE u.username = :username")
    Long countFollowersByUsername(String username);

    @Query("SELECT COUNT(f) FROM User u JOIN u.following f WHERE u.username = :username")
    Long countFollowingByUsername(String username);
}