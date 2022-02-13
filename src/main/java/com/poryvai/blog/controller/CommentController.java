package com.poryvai.blog.controller;

import com.poryvai.blog.dto.CommentWithoutPostDto;
import com.poryvai.blog.entity.*;
import com.poryvai.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/posts")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}/comments")
    public ResponseEntity<Object> saveComment(@PathVariable("id") Long postId,
                                             @RequestBody Comment comment){
        log.info("Inside saveComment of CommentController, add {}", comment, " by id{}", postId);

        if (comment == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        Comment newComment = commentService.saveComment(postId, comment);
        CommentWithoutPostDto commentWithoutPostDto = getCommentWithoutPostDto(newComment);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(commentWithoutPostDto, CREATED);
        return responseEntity;
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<Object> fetchCommentsByPostId(@PathVariable("id") Long postId) {
        log.info("Inside fetchCommentsByPostId of CommentController, all comments by id{}", postId);
        List<Comment> comments = commentService.fetchCommentsByPostId(postId);

        if (comments.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        List<CommentWithoutPostDto> commentWithoutPostDtoList = getCommentWithoutPostDtos(comments);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(commentWithoutPostDtoList, OK);
        return responseEntity;
    }

    @GetMapping(value = "{postId}/comments/{commentId}")
    public ResponseEntity<Object> fetchCommentByPostIdAndCommentId(@PathVariable("postId") Long postId,
                                                          @PathVariable("commentId") Long commentId) {
        log.info("Inside etchCommentByPostIdAndCommentId of CommentController");
        Comment comment = commentService.fetchCommentByPostIdAndCommentId(postId, commentId);

        if (comment == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        CommentWithoutPostDto commentWithoutPostDto = getCommentWithoutPostDto(comment);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(commentWithoutPostDto, OK);
        return responseEntity;
    }

    private CommentWithoutPostDto getCommentWithoutPostDto(Comment comment) {
        CommentWithoutPostDto commentWithoutPostDto = CommentWithoutPostDto.builder()
                .id(comment.getId())
                .creationDate(comment.getCreationDate())
                .text(comment.getText())
                .build();
        return commentWithoutPostDto;
    }

    private List<CommentWithoutPostDto> getCommentWithoutPostDtos(List<Comment> comments) {
        List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            CommentWithoutPostDto commentWithoutPostDto = CommentWithoutPostDto.builder()
                    .id(comment.getId())
                    .creationDate(comment.getCreationDate())
                    .text(comment.getText())
                    .build();
            commentWithoutPostDtos.add(commentWithoutPostDto);
        }
        return commentWithoutPostDtos;
    }
}
