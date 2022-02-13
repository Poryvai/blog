package com.poryvai.blog.repository;

import com.poryvai.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommnetRepository extends JpaRepository<Comment, Long> {
    @Transactional
    @Query(nativeQuery = true, value = "SELECT id, created_on, text, " +
            "post_id FROM comment WHERE post_id=?1")
    List<Comment> findAllCommentsByPostId(Long postId);

    @Transactional
    @Query(nativeQuery = true, value = "SELECT id, created_on, text, " +
            "post_id FROM comment WHERE post_id=?1 AND id=?2")
    Comment findCommentByPostIdAndCommentId(Long postId, Long id);
}
