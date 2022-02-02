package com.poryvai.blog.service;

import com.poryvai.blog.entity.Post;

import java.util.List;

public interface PostService {

    public Post savePost(Post post);

    public List<Post> fetchPostList();

    public Post updatePost(Long id, Post post);

    public void deletePostById(Long id);
}
