package com.instagram.clone.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeedPostDTO {
    private Long postId;
    private String caption;
    private String imageUrl;
    private UserSummaryDTO userSummary;
    private Long likeCount;     // Adicionando a contagem de likes
    private Long commentCount;  // Adicionando a contagem de comentários

    @Data
    @NoArgsConstructor
    public static class UserSummaryDTO {
        private Long userId;
        private String username;
        private String profilePictureUrl;
    }
}
