package com.instagram.clone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.instagram.clone.dto.UserProfileDTO;
import com.instagram.clone.model.User;
import com.instagram.clone.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("sucesso!");
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String username) {
        List<User> users = userRepository.findByUsernameStartingWithIgnoreCase(username);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Long followersCount = userRepository.countFollowersByUsername(username);
        Long followingCount = userRepository.countFollowingByUsername(username);

        UserProfileDTO userProfileDTO = new UserProfileDTO(
                user.getUsername(),
                user.getBio(),
                user.getProfilePicture(),
                followersCount,
                followingCount);

        return ResponseEntity.ok(userProfileDTO);
    }

}