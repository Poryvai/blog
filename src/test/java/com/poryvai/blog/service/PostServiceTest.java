package com.poryvai.blog.service;

import com.poryvai.blog.entity.Post;
import com.poryvai.blog.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private PostRepository mockedPostRepository;

    private DefaultPostService defaultPostService;

    @BeforeEach
    void beforeEach() {
        mockedPostRepository = mock(PostRepository.class);
        defaultPostService = new DefaultPostService(mockedPostRepository);
    }

    @Mock
    private PostRepository postRepository;
    private Post post = Post.builder()
            .id(1L)
            .title("title")
            .content("content")
            .star(false)
            .build();
    private  Post secondPost = Post.builder()
            .id(2L)
            .title("second title")
            .content("second content")
            .star(true)
            .build();
    private Post thirdPost = Post.builder()
            .id(3L)
            .title("third title")
            .content("third content")
            .star(false)
            .build();
    private Post fourthPost = Post.builder()
            .id(4L)
            .title("fourth title")
            .content("fourth content")
            .star(true)
            .build();

    List<Post> postList = List.of(post, secondPost, thirdPost, fourthPost);


    @Test
    public void whenDataIsValid_thenSavePost() {
        DefaultPostService defaultPostService = new DefaultPostService(postRepository);
        Post post = Post.builder()
                .id(1L)
                .title("title")
                .content("content")
                .star(false)
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
                .star(false)
                .build();
        Post two = Post.builder()
                .id(2L)
                .title("title two")
                .content("content two")
                .star(false)
                .build();
        List<Post> list = List.of(one, two);

        Mockito.when(postRepository.findAll()).thenReturn(list);

        List<Post> actualPosts = defaultPostService.fetchPostList();
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
                .star(false)
                .build();
        Post newPost = Post.builder()
                .content("content new")
                .title("title new")
                .id(1L)
                .star(false)
                .build();
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(oldPost));
        Mockito.when(postRepository.save(newPost)).thenReturn(newPost);
        Post expectedPost = defaultPostService.updatePost(1L, newPost);
        assertEquals(expectedPost.getContent(), oldPost.getContent());
        verify(postRepository, times(1)).save(newPost);
    }
    @Test
    void testupdatePostWithNullData() {
       Post post = Post.builder()
                .content("content")
                .title("title")
                .id(1L)
                .star(false)
                .build();

        Post blogUpd = Post.builder()
                .id(1L)
                .build();

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(post));
        Mockito.when(postRepository.save(post)).thenReturn(post);
        Post expectedPost = defaultPostService.updatePost(1L, blogUpd);
        assertEquals(expectedPost.getContent(), "content");
        assertEquals(expectedPost.getTitle(), "title");
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testFetchAllPostsByTitle() {
        defaultPostService = new DefaultPostService(postRepository);

        Mockito.when(postRepository.findAllByTitle("title")).thenReturn(List.of(post));
        List<Post> posts = defaultPostService.fetchAllPostsByTitle("title");

        assertEquals(1, posts.size());
        assertEquals(1, posts.get(0).getId());
        assertEquals("title", posts.get(0).getTitle());
        assertEquals("content", posts.get(0).getContent());
        verify(postRepository, times(1)).findAllByTitle("title");
    }

    @Test
    void testFetchAllPostsSortedByTitle() {
        defaultPostService = new DefaultPostService(postRepository);

        Mockito.when(postRepository.findAll(Sort.by(Sort.Direction.ASC, "title"))).thenReturn(postList);
        List<Post> posts = defaultPostService.fetchAllPostsSortedByTitle();

        assertEquals(4, posts.size());
        assertEquals(2, posts.get(1).getId());
        assertEquals(1, posts.get(0).getId());
        assertEquals(3, posts.get(2).getId());
        assertEquals(4, posts.get(3).getId());
        assertEquals("second title", posts.get(1).getTitle());
        assertEquals("title", posts.get(0).getTitle());
        assertEquals("third title", posts.get(2).getTitle());
        assertEquals("fourth title", posts.get(3).getTitle());
        assertEquals("second content", posts.get(1).getContent());
        assertEquals("content", posts.get(0).getContent());
        assertEquals("third content", posts.get(2).getContent());
        assertEquals("fourth content", posts.get(3).getContent());
        verify(postRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    @Test
    void testFetchAllTopPosts() {
        defaultPostService= new DefaultPostService(postRepository);

        Mockito.when(postRepository.findAllByStarTrue()).thenReturn(List.of(post, fourthPost));
        List<Post> posts = defaultPostService.fetchAllTopPosts();

        assertEquals(2, posts.size());
        assertEquals(1, posts.get(0).getId());
        assertEquals(4, posts.get(1).getId());
        assertEquals("title", posts.get(0).getTitle());
        assertEquals("fourth title", posts.get(1).getTitle());
        assertEquals("content", posts.get(0).getContent());
        assertEquals("fourth content", posts.get(1).getContent());
        assertFalse(posts.get(0).isStar());
        assertTrue(posts.get(1).isStar());
        verify(postRepository, times(1)).findAllByStarTrue();
    }

}