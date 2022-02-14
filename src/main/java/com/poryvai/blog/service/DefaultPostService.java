package com.poryvai.blog.service;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.error.PostNotFoundException;
import com.poryvai.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultPostService implements PostService{

    private final PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> fetchPostList() {
        return postRepository.findAll();
    }

    @Override
    public Post updatePost(Long id, Post post)throws PostNotFoundException {
        Post postDB = postRepository.findById(id).get();
        Optional<Post> p = postRepository.findById(id);

        if (!p.isPresent()) {
            throw new PostNotFoundException("Post Id:" + id + ", Not Available");
        }

        if(Objects.nonNull(post.getTitle()) && !"".equalsIgnoreCase(post.getTitle())){
            postDB.setTitle(post.getTitle());
        }

        if(Objects.nonNull(post.getContent()) && !"".equalsIgnoreCase(post.getContent())){
            postDB.setContent(post.getContent());
        }

        return postRepository.save(postDB);
    }

    @Override
    public void deletePostById(Long id)throws PostNotFoundException {
        postRepository.deleteById(id);
    }

    @Override
    public Post fetchPostById(Long id)throws PostNotFoundException {
        Optional<Post> post = postRepository.findById(id);

        if (!post.isPresent()){
            throw new PostNotFoundException("Post Id:" + id + ", Not Available");
        }
        return post.get();
    }

    @Override
    public List<Post> fetchAllPostsByTitle(String title) {
        return postRepository.findAllByTitle(title);
    }

    @Override
    public List<Post> fetchAllPostsSortedByTitle() {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    @Override
    public List<Post> fetchAllTopPosts() {
        return postRepository.findAllByStarTrue();
    }

    @Override
    public Post updatePostSetStar(Long id) {
        return postRepository.updatePostSetStar(id);
    }

    @Override
    public Post updatePostUnsetStar(Long id) {
        return postRepository.updatePostUnsetStar(id);
    }

}
