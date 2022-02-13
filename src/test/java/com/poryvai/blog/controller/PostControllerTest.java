package com.poryvai.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poryvai.blog.entity.Post;
import com.poryvai.blog.service.DefaultPostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DefaultPostService defaultPostService;
    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
                .star(true)
                .build();
    }

    @Test
    public void testSavePost() throws Exception {
        Post inputPost  = Post.builder()
                .title("title one")
                .content("content one")
                .build();

        Mockito.when(defaultPostService.savePost(inputPost)).thenReturn(post);

        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"title\":\"title one\",\n" +
                        "\t\"content\":\"content one\"\n" +
                        "}"))
                .andExpect(status().isCreated());
        verify(defaultPostService).savePost(any(Post.class));
    }

    @Test
    void testFetchPosttList() throws Exception {
        Post post = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
                .build();
        Post secondPost = Post.builder()
                .id(2L)
                .title("title two")
                .content("content two")
                .build();

        Mockito.when(defaultPostService.fetchPostList()).thenReturn(List.of(post, secondPost));

        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("title one"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("title two"));
        verify(defaultPostService, times(1)).fetchPostList();
    }

    @Test
    void testFetchPostListIfPostIsEmpty () throws Exception {
        List<Post> posts = new ArrayList<>();
        when(defaultPostService.fetchPostList())
                .thenReturn(posts);

        mockMvc.perform(get("/api/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(defaultPostService, times(1)).fetchPostList();
    }

    @Test
    public void testUpdatePost() throws Exception {
        Post currentPost  = Post.builder()
                .title("title two")
                .content("content two")
                .build();
        Mockito.when(defaultPostService.updatePost(1L,post)).thenReturn(currentPost);

        mockMvc.perform(put("/api/v1/posts/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"title\":\"title two\",\n" +
                                "\t\"content\":\"content two\"\n" +
                                "}"))
                        .andExpect(MockMvcResultMatchers.status().isOk());
        verify(defaultPostService, times(1)).updatePost(1L, currentPost);
    }

    @Test
    public void testDeletePostById() throws Exception {
        mockMvc.perform(delete("/api/v1/posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(defaultPostService).deletePostById(any(Long.class));
    }

    @Test
    void testFetchAllPostsSortedByTitle() throws Exception {
        List<Post> posts = new ArrayList<>();

        Post postOne = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
                .star(false)
                .build();
        posts.add(postOne);

        Post postTwo = Post.builder()
                .id(2L)
                .title("title two")
                .content("title two")
                .star(false)
                .build();
        posts.add(postTwo);

        Post postThree = Post.builder()
                .id(3L)
                .title("title three")
                .content("content three")
                .star(true)
                .build();
        posts.add(postThree);

        Mockito.when(defaultPostService.fetchAllPostsSortedByTitle()).thenReturn(posts);
        mockMvc.perform( get("/api/v1/posts/?sort=title")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("title one"))
                .andExpect(jsonPath("$[1].title").value("title two"))
                .andExpect(jsonPath("$[2].title").value("title three"));
        verify(defaultPostService, times(1)).fetchAllPostsSortedByTitle();
    }


    @Test
    void testFetchAllPostsSortedByTitleIfPostsIsEmpty() throws Exception {
        List<Post> posts = new ArrayList<>();

        when(defaultPostService.fetchAllPostsSortedByTitle()).thenReturn(posts);
        mockMvc.perform( get("/api/v1/posts/?sort=title")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(defaultPostService, times(1)).fetchAllPostsSortedByTitle();
    }


    @Test
    void testFetchAllPostsByTitle() throws Exception {
        List<Post> posts = new ArrayList<>();

        Post postOne = Post.builder()
                .id(1L)
                .title("post one")
                .content("content one")
                .star(false)
                .build();
        posts.add(postOne);

        when(defaultPostService.fetchAllPostsByTitle("post one")).thenReturn(posts);
        mockMvc.perform( get("/api/v1/posts/?title={title}", "post one")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("post one"));
        verify(defaultPostService, times(1)).fetchAllPostsByTitle("post one");
    }

    @Test
    void testFetchAllPostsByTitleIfPostsIsEmpty() throws Exception {
        List<Post> posts = new ArrayList<>();

        when(defaultPostService.fetchAllPostsByTitle("post one")).thenReturn(posts);
        mockMvc.perform( get("/api/v1/posts/?title={title}", "post one")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(defaultPostService, times(1)).fetchAllPostsByTitle("post one");
    }

    @Test
    void testFetchAllTopPost() throws Exception {
        List<Post> posts = new ArrayList<>();

        Post postOne = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
                .star(false)
                .build();
        posts.add(postOne);

        Post postTwo = Post.builder()
                .id(2L)
                .title("title two")
                .content("title two")
                .star(false)
                .build();
        posts.add(postTwo);

        Post postThree = Post.builder()
                .id(3L)
                .title("title three")
                .content("content three")
                .star(true)
                .build();
        posts.add(postThree);

        when(defaultPostService.fetchAllTopPosts()).thenReturn(posts);
        mockMvc.perform( get("/api/v1/posts/star")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].star").value(false))
                .andExpect(jsonPath("$[1].star").value(false))
                .andExpect(jsonPath("$[2].star").value(true));
        verify(defaultPostService, times(1)).fetchAllTopPosts();
    }

    @Test
    void testFetchAllTopPostsIfPostsIsEmpty() throws Exception {
        List<Post> posts = new ArrayList<>();

        when(defaultPostService.fetchAllTopPosts()).thenReturn(posts);
        mockMvc.perform( get("/api/v1/posts/star")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(defaultPostService, times(1)).fetchAllTopPosts();
    }

    @Test
    void testUpdatePostAddStar() throws Exception {
        Post postOne = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
                .star(true)
                .build();

        when(defaultPostService.updatePostSetStar(1L)).thenReturn(postOne);
        mockMvc.perform(put("/api/v1/posts/{id}/star", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.star").value(true))
                .andExpect(status().isOk());
        verify(defaultPostService, times(1)).updatePostSetStar(1L);

    }

    @Test
    void testUpdatePostAddStarIfPostIsNull() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .put("/api/v1/posts/{id}/star", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePostUnsetStar() throws Exception {
        Post postOne = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
                .star(false)
                .build();

        when(defaultPostService.updatePostUnsetStar(1L)).thenReturn(postOne);
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}/star", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.star").value(false))
                .andExpect(status().isOk());
        verify(defaultPostService, times(1)).fetchPostById(1L);
    }

    @Test
    void testUpdatePostUnsetStarIfPostIsNull() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/v1/posts/{id}/star", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }
}