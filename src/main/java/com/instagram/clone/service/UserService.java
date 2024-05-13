package com.instagram.clone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.clone.model.User;
import com.instagram.clone.repository.UserRepository;

@Service
public class UserService {

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

    public User updateUser(String username, String email, String password,  String fullName, String bio, String profilePicture) {
        User existingUser = userRepository.findByUsername(username)
                                   .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(email);
        existingUser.setFullName(fullName);
        existingUser.setPassword(password);
        existingUser.setFullName(fullName);
        existingUser.setBio(bio);
        existingUser.setProfilePicture(profilePicture);
        return userRepository.save(existingUser);
    }
}