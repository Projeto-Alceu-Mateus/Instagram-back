package com.instagram.clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagram.clone.model.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    // Exemplo de método para contar os likes de um post
    int countByPostId(Long postId);
}