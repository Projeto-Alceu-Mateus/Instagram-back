package com.instagram.clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.instagram.clone.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Exemplo de busca de postagens por usuário
    List<Post> findAllByUserId(Long userId);
}