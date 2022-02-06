package com.poryvai.blog.controller;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.error.PostNotFoundException;
import com.poryvai.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping
    public ResponseEntity<Post> savePost(@Valid @RequestBody Post post){
        Post newPost = postService.savePost(post);
        return ResponseEntity.status(201).body(newPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> fetchDepartmentList(){
        List<Post> posts = postService.fetchPostList();
        return ResponseEntity.ok().body(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id,
                           @RequestBody Post post)throws PostNotFoundException {
        Post newDataPost = postService.updatePost(id, post);
        return ResponseEntity.ok().body(newDataPost);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long id)throws PostNotFoundException {
        postService.deletePostById(id);
    }
}
