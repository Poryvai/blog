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
    public ResponseEntity<Object> savePost(@RequestBody Post post){
        log.info("Inside savePost of PostController, add {}", post);
        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        Post newPost = postService.savePost(post);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(newPost, CREATED);
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<List<Post>> fetchPostList(@RequestParam(value = "title", required = false) String title,
                              @RequestParam(value = "sort", required = false) String sort) {
        List<Post> posts;

        log.info("Inside fetchAllPostsByTitle of PostController");
        if (title != null) {
            posts = postService.fetchAllPostsByTitle(title);
            if (posts.isEmpty()){
                return new ResponseEntity<>(NOT_FOUND);
            }
        } else if (sort != null) {
            log.info("Inside fetchAllPostsSortedByTitle of PostController");
            posts = postService.fetchAllPostsSortedByTitle();
            if (posts.isEmpty()){
                return new ResponseEntity<>(NOT_FOUND);
            }
        } else {
            log.info("Inside fetchPostList of PostController");
            posts = postService.fetchPostList();
            if (posts.isEmpty()){
                return new ResponseEntity<>(NOT_FOUND);
            }
        }

        ResponseEntity<List<Post>> responseEntity = new ResponseEntity<>(posts, OK);
        return responseEntity;
       // return ResponseEntity.status(OK).body(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") Long id,
                           @RequestBody Post post)throws PostNotFoundException {
        log.info("Inside updatePost of PostController, update data by id {}", id);
        Post newDataPost = postService.updatePost(id, post);

        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(newDataPost, OK);
        return responseEntity;
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

        if (postId == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        ResponseEntity<Post> responseEntity = new ResponseEntity<>(postId, OK);
        return responseEntity;
    }

    @GetMapping("/star")
    public ResponseEntity<Object> fetchAllTopPosts() {
       log.info("Inside fetchAllTopPosts of PostController");
        List<Post> posts = postService.fetchAllTopPosts();

        if (posts.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(posts, OK);
        return responseEntity;
    }

    @PutMapping("/{id}/star")
    public ResponseEntity<Object> updatePostSetStar(@PathVariable Long id) {
        log.info("Inside updatePostSetStar of PostController, get post by id markered star {}", id);
        Post post = postService.updatePostSetStar(id);

        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(post, OK);
        return responseEntity;
    }

    @DeleteMapping("/{id}/star")
    public ResponseEntity<Object> updatePostUnsetStar(@PathVariable Long id) {
        log.info("Inside updatePostUnsetStar of PostController, get post by id unmarkered star {}", id);
        Post post = postService.updatePostUnsetStar(id);

        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(post, OK);
        return responseEntity;
    }

}
