package com.poryvai.blog.service;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.error.PostNotFoundException;

import java.util.List;

public interface PostService {

    public Post savePost(Post post);

    public List<Post> fetchPostList();

    public Post updatePost(Long id, Post post)throws PostNotFoundException;

    public void deletePostById(Long id)throws PostNotFoundException;

    public Post fetchPostById(Long departmentId)throws PostNotFoundException;

    public List<Post> fetchAllPostsByTitle(String title);

    public List<Post> fetchAllPostsSortedByTitle();

    public List<Post> fetchAllTopPosts();

    public Post updatePostSetStar(Long id);

    public Post updatePostUnsetStar(Long id);
}
