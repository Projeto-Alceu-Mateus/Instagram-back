package com.instagram.clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagram.clone.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Buscar comentários por post
    List<Comment> findByPostId(Long postId);
    void deleteByPostId(Long postId);
}