package com.poryvai.blog.repository;

import com.poryvai.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByTitle(String title);
    List<Post> findAllByStarTrue();

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Post SET star=true WHERE id=?1 RETURNING *")
    Post updatePostSetStar(Long id);

    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Post SET star=false WHERE id=?1 RETURNING *")
    Post updatePostUnsetStar(Long id);

}
