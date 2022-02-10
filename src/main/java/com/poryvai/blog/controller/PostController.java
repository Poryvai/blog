package com.poryvai.blog.controller;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.error.PostNotFoundException;
import com.poryvai.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Post> savePost(@RequestBody Post post){
        log.info("Inside savePsot of PostController, add {}", post);
        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        Post newPost = postService.savePost(post);
        return ResponseEntity.status(201).body(newPost);
    }

    @GetMapping
    public ResponseEntity<List<Post>> fetchPostList(@RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "sort", required = false) String sort) {
        List<Post> posts;
        log.info("Inside fetchAllPostsByTitle of PostController");
        if (title != null) {
            posts = postService.fetchAllPostsByTitle(title);
        } else if (sort != null) {
            log.info("Inside fetchAllPostsSortedByTitle of PostController");
            posts = postService.fetchAllPostsSortedByTitle();
        } else {
            log.info("Inside fetchPostList of PostController");
            posts = postService.fetchPostList();
        }

        return ResponseEntity.ok().body(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id,
                           @RequestBody Post post)throws PostNotFoundException {
        log.info("Inside updatePost of PostController, update data by id {}", id);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Post newDataPost = postService.updatePost(id, post);
        return ResponseEntity.ok().body(newDataPost);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long id)throws PostNotFoundException {
        log.info("Inside deletePostById of PostController, delete post by id {}", id);
        postService.deletePostById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> fetchPostById(@PathVariable("id") Long id) throws PostNotFoundException {
        log.info("Inside fetchPostById of PostController, get post by id {}", id);
        Post postId = postService.fetchPostById(id);
        return ResponseEntity.ok().body(postId);
    }

}
