package com.instagram.clone.service;

import com.instagram.clone.dto.FeedPostDTO;
import com.instagram.clone.model.Post;
import com.instagram.clone.repository.PostRepository;
import com.instagram.clone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FeedPostDTO> getFeedForUserId(Long userId) {
        List<Long> userIdsFollowed = userRepository.findUserIdsFollowedBy(userId);
        List<Post> posts = postRepository.findAllByUserIdIn(userIdsFollowed);
        return posts.stream().map(this::convertToFeedPostDTO).collect(Collectors.toList());
    }

    private FeedPostDTO convertToFeedPostDTO(Post post) {
        FeedPostDTO dto = new FeedPostDTO();
        dto.setPostId(post.getId());
        dto.setCaption(post.getCaption());
        dto.setImageUrl(post.getImageUrl());
        FeedPostDTO.UserSummaryDTO userSummary = new FeedPostDTO.UserSummaryDTO();
        userSummary.setUserId(post.getUser().getId());
        userSummary.setUsername(post.getUser().getUsername());
        userSummary.setProfilePictureUrl(post.getUser().getProfilePicture());
        dto.setUserSummary(userSummary);
        return dto;
    }
}
