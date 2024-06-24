package com.instagram.clone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.clone.dto.EditUserDTO;
import com.instagram.clone.dto.UserProfileDTO;
import com.instagram.clone.infra.security.TokenService;
import com.instagram.clone.model.User;
import com.instagram.clone.repository.LikeRepository;
import com.instagram.clone.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String email, String username, String password, String fullName) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Criptografar a senha antes de salvar
        newUser.setFullName(fullName);
        return userRepository.save(newUser);
    }

    public void followUserByUsername(String currentUsername, String targetUsername) throws Exception {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new Exception("Current user not found"));
        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new Exception("Target user not found"));

        // Verificar se já está seguindo
        if (currentUser.getFollowing().stream().anyMatch(u -> u.getId().equals(targetUser.getId()))) {
            throw new Exception("Already following");
        }

        userRepository.followUser(currentUser.getId(), targetUser.getId());
    }

    public void unfollowUserByUsername(String currentUsername, String targetUsername) throws Exception {
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new Exception("Current user not found"));
        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new Exception("Target user not found"));

        userRepository.unfollowUser(currentUser.getId(), targetUser.getId());
    }

    public List<UserProfileDTO> searchUsers(String username) {
        List<User> users = userRepository.findByUsernameStartingWithIgnoreCase(username);
        return users.stream()
                .map(user -> new UserProfileDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getBio(),
                        user.getProfilePicture(),
                        (long) user.getFollowers().size(),
                        (long) user.getFollowing().size()))
                .collect(Collectors.toList());
    }

    public Boolean verifyIfUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public EditUserDTO updateUserProfile(String username, EditUserDTO updateRequest) {
       

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateRequest.getUsername() != null && !updateRequest.getUsername().isEmpty() && !updateRequest.getUsername().equals(user.getUsername())) {
            // Verifique se o novo username já está em uso
            User existingUser = userRepository.findByUsername(updateRequest.getUsername()).orElse(null);
            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                throw new RuntimeException("Username already in use");
            }
            user.setUsername(updateRequest.getUsername());
        }
        if (updateRequest.getBio() != null && !updateRequest.getBio().equals(user.getBio())) {
            user.setBio(updateRequest.getBio());
        }
        if (updateRequest.getProfilePicture() != null && !updateRequest.getProfilePicture().equals(user.getProfilePicture())) {
            user.setProfilePicture(updateRequest.getProfilePicture());
        }

        userRepository.save(user);

        // Gerar novo token JWT
        String newToken = tokenService.generateToken(user);

        EditUserDTO responseDTO = new EditUserDTO();
        responseDTO.setUsername(user.getUsername());
        responseDTO.setBio(user.getBio());
        responseDTO.setProfilePicture(user.getProfilePicture());
        responseDTO.setToken(newToken);

        return responseDTO;
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Remove todos os likes do usuário
        likeRepository.deleteByUser(user);

        // Remove o usuário dos seguidores e deixar de seguir todos os outros usuários
        user.getFollowers().forEach(follower -> {
            follower.getFollowing().remove(user);
        });
        user.getFollowing().clear();

        // Remove o usuário do banco de dados
        userRepository.delete(user);
    }
}