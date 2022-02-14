package com.poryvai.blog.controller;

import com.poryvai.blog.dto.CommentWithoutPostDto;
import com.poryvai.blog.dto.PostWithCommentsDto;
import com.poryvai.blog.dto.PostWithoutCommentsDto;
import com.poryvai.blog.entity.Comment;
import com.poryvai.blog.entity.Post;
import com.poryvai.blog.error.PostNotFoundException;
import com.poryvai.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Object> savePost(@RequestBody Post post){
        log.info("Inside savePost of PostController, add {}", post);
        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        Post newPost = postService.savePost(post);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(newPost, CREATED);
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<Object> fetchPostList(@RequestParam(value = "title", required = false) String title,
                                                @RequestParam(value = "sort", required = false) String sort) {
        List<Post> posts;

        log.info("Inside fetchAllPostsByTitle of PostController");
        if (title != null) {

            posts = postService.fetchAllPostsByTitle(title);
            if (posts.isEmpty()) {
                return new ResponseEntity<>(NOT_FOUND);
            } else {
                List<PostWithCommentsDto> postWithCommentsDto = getPostWithCommentsDtos(posts);
                return ResponseEntity.ok(postWithCommentsDto);
            }
        } else if (sort != null) {
            log.info("Inside fetchAllPostsSortedByTitle of PostController");
            posts = postService.fetchAllPostsSortedByTitle();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(NOT_FOUND);
            } else {
                List<PostWithCommentsDto> postWithCommentsDtos = getPostWithCommentsDtos(posts);
                return ResponseEntity.ok(postWithCommentsDtos);
            }
        } else {
            log.info("Inside fetchPostList of PostController");
            posts = postService.fetchPostList();
            if (posts.isEmpty()) {
                return new ResponseEntity<>(NOT_FOUND);
            }
            List<PostWithCommentsDto> postWithCommentsDto = getPostWithCommentsDtos(posts);
            ResponseEntity<Object> responseEntity = new ResponseEntity<>(postWithCommentsDto, OK);
            return responseEntity;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable("id") Long id,
                           @RequestBody Post post)throws PostNotFoundException {
        log.info("Inside updatePost of PostController, update data by id {}", id);
        Post newDataPost = postService.updatePost(id, post);

        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(newDataPost, OK);
        return responseEntity;
    }

    @DeleteMapping("/{id}")
    public void deletePostById(@PathVariable("id") Long id)throws PostNotFoundException {
        log.info("Inside deletePostById of PostController, delete post by id {}", id);
        postService.deletePostById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> fetchPostById(@PathVariable("id") Long id) throws PostNotFoundException {
        log.info("Inside fetchPostById of PostController, get post by id {}", id);
        Post postId = postService.fetchPostById(id);

        if (postId == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        PostWithoutCommentsDto postWithoutCommentsDto = getPostWithOutCommentsDto(postId);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(postWithoutCommentsDto, OK);
        return responseEntity;
    }

    @GetMapping("/star")
    public ResponseEntity<Object> fetchAllTopPosts() {
       log.info("Inside fetchAllTopPosts of PostController");
        List<Post> posts = postService.fetchAllTopPosts();

        if (posts.isEmpty()) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        List<PostWithCommentsDto> postWithCommentsDtos = getPostWithCommentsDtos(posts);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(postWithCommentsDtos, OK);
        return responseEntity;
    }

    @PutMapping("/{id}/star")
    public ResponseEntity<Object> updatePostSetStar(@PathVariable Long id) {
        log.info("Inside updatePostSetStar of PostController, get post by id markered star {}", id);
        Post post = postService.updatePostSetStar(id);

        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        PostWithCommentsDto postWithCommentsDto = getPostWithCommentsDto(post);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(postWithCommentsDto, OK);
        return responseEntity;
    }

    @DeleteMapping("/{id}/star")
    public ResponseEntity<Object> updatePostUnsetStar(@PathVariable Long id) {
        log.info("Inside updatePostUnsetStar of PostController, get post by id unmarkered star {}", id);
        Post post = postService.updatePostUnsetStar(id);

        if (post == null) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        PostWithCommentsDto postWithCommentsDto = getPostWithCommentsDto(post);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(postWithCommentsDto, OK);
        return responseEntity;
    }

    @GetMapping("/{id}/full")
    public ResponseEntity<Object> fetchPostByIdWithAllComments(@PathVariable("id") Long id) throws PostNotFoundException {
        Post post = postService.fetchPostById(id);

        if (post == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        PostWithCommentsDto postWithCommentsDto = getPostWithCommentsDto(post);
        log.info("Inside fetchPostByIdWithAllComments of PostController, get full post {} ", postWithCommentsDto);
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(postWithCommentsDto, OK);
        return responseEntity;
        }

    private List<PostWithCommentsDto> getPostWithCommentsDtos(List<Post> posts) {
        List<PostWithCommentsDto> postWithCommentsDtos = new ArrayList<>(posts.size());
        for (Post post : posts) {
            List<Comment> comments = post.getComments();
            List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>(comments.size());
            for (Comment comment : comments) {
                CommentWithoutPostDto commentWithoutPostDto = CommentWithoutPostDto.builder()
                        .id(comment.getId())
                        .text(comment.getText())
                        .creationDate(comment.getCreationDate())
                        .build();
                commentWithoutPostDtos.add(commentWithoutPostDto);
            }

            PostWithCommentsDto postWithCommentsDto = PostWithCommentsDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .star(post.isStar())
                    .comments(commentWithoutPostDtos)
                    .build();

            postWithCommentsDtos.add(postWithCommentsDto);
        }
        return postWithCommentsDtos;
    }

    private PostWithCommentsDto getPostWithCommentsDto(Post post) {
        List<Comment> comments = post.getComments();
        List<CommentWithoutPostDto> commentWithoutPostDtos = new ArrayList<>(comments.size());
        for (Comment comment : comments) {
            CommentWithoutPostDto commentWithoutPostDto = CommentWithoutPostDto.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .creationDate(comment.getCreationDate())
                    .build();
            commentWithoutPostDtos.add(commentWithoutPostDto);
        }
        PostWithCommentsDto postWithCommentsDto = PostWithCommentsDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .star(post.isStar())
                .comments(commentWithoutPostDtos)
                .build();

        return postWithCommentsDto;
    }

    private PostWithoutCommentsDto getPostWithOutCommentsDto(Post post) {
        PostWithoutCommentsDto postWithoutCommentsDto = PostWithoutCommentsDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .star(post.isStar())
                .build();
        return postWithoutCommentsDto;
    }

}
