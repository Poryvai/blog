package com.poryvai.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentWithPostDto {
    private Long id;
    private String text;
    private LocalDateTime creationDate;
}
