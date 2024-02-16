package com.rewcode.blog.controller;

import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.payload.PostResponse;
import com.rewcode.blog.service.PostService;
import com.rewcode.blog.utils.AppConstants;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto){
        PostDto savedPost = postService.createPost(postDto);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PostResponse> getAllPostsPagination(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                              @RequestParam(value = AppConstants.DEFAULT_PAGE_NUMBER, defaultValue = "0") int pageNumber,
                                                              @RequestParam(value = AppConstants.DEFAULT_PAGE_SORT_BY_FIELD, defaultValue = "id", required = false) String sortBy,
                                                              @RequestParam(value = AppConstants.DEFAULT_PAGE_SORT_DIRECTION, defaultValue = "asc", required = false) String sortDirection){
        PostResponse postResponse = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDirection);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        PostDto postDto = postService.getPostById(id);
        return ResponseEntity.ok(postDto);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<PostDto> updatePost(@PathVariable Long id,  @RequestBody PostDto postDto){
        PostDto updatedPostDto = postService.updatePost(id, postDto);
        return ResponseEntity.ok(updatedPostDto);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deletePost(@PathVariable Long id){
        postService.deletePostById(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
