package com.poryvai.blog.service;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private PostRepository mockedPostRepository;

    @Autowired
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        mockedPostRepository = mock(PostRepository.class);
        postService = new DefaultPostService(mockedPostRepository);
    }

    @BeforeEach
    void setUp() {

        Post post = Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();
        Post secondPost = Post.builder()
                .id(2L)
                .title("second title")
                .content("second content")
                .build();
        Post thirdPost = Post.builder()
                .id(3L)
                .title("third title")
                .content("third content")
                .build();
        Post fourthPost = Post.builder()
                .id(1L)
                .title("fourth title")
                .content("fourth content")
                .build();

        List<Post> postList = List.of(post, secondPost, thirdPost);

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(postList.get(0)));
        Mockito.when(postRepository.save(post)).thenReturn(post);
        Mockito.when(postRepository.findAll()).thenReturn(postList);
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(fourthPost);
    }

    @Test
    public void whenDataIsValid_thenSavePost() {
        DefaultPostService defaultPostService = new DefaultPostService(postRepository);
        Post post = Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .build();
        defaultPostService.savePost(post);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetAllPost() {
        DefaultPostService defaultPostService = new DefaultPostService(postRepository);
        Post one = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
                .build();
        Post two = Post.builder()
                .id(2L)
                .title("title two")
                .content("content two")
                .build();
        List<Post> list = List.of(one, two);

        when(postRepository.findAll()).thenReturn(list);

        List<Post> actualPosts = postService.fetchPostList();
        assertEquals(list.size(), actualPosts.size());
        assertEquals(list.get(1).getTitle(), actualPosts.get(1).getTitle());
        assertEquals(list.get(1).getContent(), actualPosts.get(1).getContent());
        assertEquals(list.get(1).getId(), actualPosts.get(1).getId());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    void testUdatePost() {
        Post oldPost = Post.builder()
                .content("content")
                .title("title")
                .id(1L)
                .build();
        Post newPost = Post.builder()
                .content("content new")
                .title("title new")
                .id(1L)
                .build();
        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(oldPost));
        when(postRepository.save(newPost)).thenReturn(newPost);
        Post expectedPost = postService.updatePost(1L, newPost);
        assertEquals(expectedPost.getContent(), oldPost.getContent());
        verify(postRepository, times(1)).save(newPost);
    }
    @Test
    void testupdatePostWithNullData() {
       Post post = Post.builder()
                .content("content")
                .title("title")
                .id(1L)
                .build();

        Post blogUpd = Post.builder()
                .id(1L)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));
        when(postRepository.save(post)).thenReturn(post);
        Post expectedPost = postService.updatePost(1L, blogUpd);
        assertEquals(expectedPost.getContent(), "content");
        assertEquals(expectedPost.getTitle(), "title");
        verify(postRepository, times(1)).save(post);
    }



}