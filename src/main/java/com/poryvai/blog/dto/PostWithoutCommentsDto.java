package com.poryvai.blog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostWithoutCommentsDto {
    private Long id;
    private String title;
    private String content;
    private boolean star;
}
