package com.poryvai.blog.service;

import com.poryvai.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    public Comment saveComment(Long postId, Comment comment);

    List<Comment> fetchCommentsByPostId(Long postId);

    Comment fetchCommentByPostIdAndCommentId(Long postId, Long commentId);
}
