package com.poryvai.blog.service;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.error.PostNotFoundException;

import java.util.List;

public interface PostService {

     Post savePost(Post post);

     List<Post> fetchPostList();

     Post updatePost(Long id, Post post)throws PostNotFoundException;

     void deletePostById(Long id)throws PostNotFoundException;

     Post fetchPostById(Long id)throws PostNotFoundException;

     List<Post> fetchAllPostsByTitle(String title);

     List<Post> fetchAllPostsSortedByTitle();

     List<Post> fetchAllTopPosts();

     Post updatePostSetStar(Long id);

     Post updatePostUnsetStar(Long id);
}
