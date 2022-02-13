package com.poryvai.blog.service;

import com.poryvai.blog.entity.Comment;
import com.poryvai.blog.entity.Post;
import com.poryvai.blog.repository.CommnetRepository;
import com.poryvai.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultCommentService implements CommentService{

    private final CommnetRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public Comment saveComment(Long postId, Comment comment) {
        Post postDB = postRepository.findById(postId).get();
        comment.setPost(postDB);
        comment.setCreationDate(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> fetchCommentsByPostId(Long postId) {
        return commentRepository.findAllCommentsByPostId(postId);
    }

    @Override
    public Comment fetchCommentByPostIdAndCommentId(Long postId, Long commentId) {
        return commentRepository.findCommentByPostIdAndCommentId(postId, commentId);
    }
}
