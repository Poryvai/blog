package com.poryvai.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostWithCommentsDto {
    private Long id;
    private String title;
    private String content;
    private boolean star;
    private List<CommentWithoutPostDto> comments;
}
