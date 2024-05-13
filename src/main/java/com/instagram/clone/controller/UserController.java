package com.instagram.clone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.instagram.clone.dto.FeedPostDTO;
import com.instagram.clone.dto.PostDTO;
import com.instagram.clone.dto.UserProfileDTO;
import com.instagram.clone.dto.UserUpdateRequestDTO;
import com.instagram.clone.model.Post;
import com.instagram.clone.model.User;
import com.instagram.clone.repository.PostRepository;
import com.instagram.clone.repository.UserRepository;
import com.instagram.clone.service.FeedService;
import com.instagram.clone.service.PostService;
import com.instagram.clone.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PostRepository PostRepository;
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

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
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getProfilePicture(),
                followersCount,
                followingCount);

        return ResponseEntity.ok(userProfileDTO);
    }

    @PostMapping("/{currentUsername}/follow/{targetUsername}")
    public ResponseEntity<?> followUserByUsername(@PathVariable String currentUsername,
            @PathVariable String targetUsername) {
        try {
            userService.followUserByUsername(targetUsername, currentUsername);
            return ResponseEntity.ok("User followed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{currentUsername}/unfollow/{targetUsername}")
    public ResponseEntity<?> unfollowUserByUsername(@PathVariable String currentUsername,
            @PathVariable String targetUsername) {
        try {
            userService.unfollowUserByUsername(targetUsername, currentUsername);
            return ResponseEntity.ok("User unfollowed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{username}/isFollowing/{targetUsername}")
    public ResponseEntity<Boolean> isUserFollowing(@PathVariable String username, @PathVariable String targetUsername) {
        User targetUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));

        User loggedInUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logged in user not found"));

        boolean isFollowing = userRepository.isFollowing(loggedInUser.getId(), targetUser.getId());
        return ResponseEntity.ok(isFollowing);
    }

    @PostMapping("/{username}/newPost")
    public ResponseEntity<?> createNewPost(@PathVariable String username, @RequestBody PostDTO postDTO) {
        try {
            postService.createPost(username, postDTO);
            return ResponseEntity.ok("Post created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating post: " + e.getMessage());
        }
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));

        List<Post> posts = PostRepository.findAllByUserId(user.getId());
        return ResponseEntity.ok(posts);
    }

    @Autowired
    private FeedService feedService;

    @GetMapping("/{username}/feed")
    public ResponseEntity<List<FeedPostDTO>> getUserFeed(@PathVariable String username) {

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));
            List<FeedPostDTO> feedPosts = feedService.getFeedForUserId(user.getId());
            return ResponseEntity.ok(feedPosts);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{username}/update")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody UserUpdateRequestDTO request) {
        User updatedUser = userService.updateUser(username, request.getEmail(), request.getPassword(), request.getFullName(), request.getBio(), request.getProfilePicture());
        return ResponseEntity.ok(updatedUser);
    }
}