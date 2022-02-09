package com.poryvai.blog.controller;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefaultPostService defaultPostService;
    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .title("title one")
                .content("content one")
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
}