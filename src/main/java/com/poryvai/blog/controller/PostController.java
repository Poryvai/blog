package com.poryvai.blog.controller;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/posts")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping
    public Post savePost(@Valid @RequestBody Post post){
        return postService.savePost(post);
    }

    @GetMapping
    public List<Post> fetchDepartmentList(){
        return postService.fetchPostList();
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable("id") Long id,
                                       @RequestBody Post post){
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long id) {
        postService.deletePostById(id);
    }
}
