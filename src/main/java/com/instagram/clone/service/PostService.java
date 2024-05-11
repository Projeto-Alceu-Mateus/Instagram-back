package com.instagram.clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.instagram.clone.dto.PostDTO;
import com.instagram.clone.model.Post;
import com.instagram.clone.model.User;
import com.instagram.clone.repository.PostRepository;
import com.instagram.clone.repository.UserRepository;

@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public void createPost(String username, PostDTO postDTO) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));

        Post post = new Post();
        post.setCaption(postDTO.getCaption()); // Usa o getter para obter a legenda
        post.setImageUrl(postDTO.getImageUrl()); // Usa o getter para obter a URL da imagem
        post.setUser(user); // Associa o usuário ao post

        postRepository.save(post); // Salva o post no banco de dados
    }

    public void getPostsById(Long userId) {
        
    }
}
