package com.instagram.clone.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.clone.dto.UserProfileDTO;
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

    public List<UserProfileDTO> searchUsers(String username) {
        List<User> users = userRepository.findByUsernameStartingWithIgnoreCase(username);
        return users.stream()
                    .map(user -> new UserProfileDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getBio(),
                        user.getProfilePicture(),
                        (long) user.getFollowers().size(),
                        (long) user.getFollowing().size()
                    ))
                    .collect(Collectors.toList());
    }
}