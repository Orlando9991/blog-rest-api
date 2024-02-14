package com.rewcode.blog.controller;

import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.service.PostService;
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
    public ResponseEntity<List<PostDto>> getAllPosts(){
        List<PostDto> postDtoList = postService.getAllPosts();
        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping("{id}")
    public  ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        PostDto postDto = postService.getPostById(id);
        return ResponseEntity.ok(postDto);
    }

    @PutMapping("{id}")
    public  ResponseEntity<PostDto> updatePost(@PathVariable Long id,  @RequestBody PostDto postDto){
        PostDto updatedPostDto = postService.updatePost(id, postDto);
        return ResponseEntity.ok(updatedPostDto);
    }

    @DeleteMapping("{id}")
    public  ResponseEntity<String> deletePost(@PathVariable Long id){
        postService.deletePostById(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
