package com.instagram.clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_followers (user_id, follower_id) VALUES (:userId, :followerId)", nativeQuery = true)
    void followUser(Long userId, Long followerId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_followers WHERE user_id = :userId AND follower_id = :followerId", nativeQuery = true)
    void unfollowUser(Long userId, Long followerId);

    @Query("SELECT COUNT(f) > 0 FROM User u JOIN u.following f WHERE u.id = :userId AND f.id = :targetId")
    boolean isFollowing(@Param("userId") Long userId, @Param("targetId") Long targetId);

    @Query(value = "SELECT user_id FROM user_followers WHERE follower_id = :userId", nativeQuery = true)
    List<Long> findUserIdsFollowedBy(Long userId);

    boolean existsByUsername(String username);
}