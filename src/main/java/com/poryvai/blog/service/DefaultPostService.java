package com.poryvai.blog.service;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DefaultPostService implements PostService{

    @Autowired
    PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> fetchPostList() {
        return postRepository.findAll();
    }

    @Override
    public Post updatePost(Long id, Post post) {
        Post postDB = postRepository.findById(id).get();

        if(Objects.nonNull(post.getTitle()) && !"".equalsIgnoreCase(post.getTitle())){
            postDB.setTitle(post.getTitle());
        }

        if(Objects.nonNull(post.getContent()) && !"".equalsIgnoreCase(post.getContent())){
            postDB.setContent(post.getContent());
        }

        return postRepository.save(postDB);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
}
