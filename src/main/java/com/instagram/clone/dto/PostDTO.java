package com.instagram.clone.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDTO {

    private String caption;
    private String imageUrl; // Certifique-se de que este campo está correto e necessário

}
