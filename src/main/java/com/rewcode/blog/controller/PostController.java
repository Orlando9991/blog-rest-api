package com.rewcode.blog.controller;

import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.payload.PostResponse;
import com.rewcode.blog.service.PostService;
import com.rewcode.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/posts")
@Tag(
        name = "CRUD REST APIs for Post Resource"
)
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @Operation(
            summary = "Create Post REST API"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 created"
    )
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto){
        PostDto savedPost = postService.createPost(postDto);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @Operation(summary = "Get All Post id REST API")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 success"
    )
    @GetMapping
    public ResponseEntity<PostResponse> getAllPostsPagination(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
                                                              @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
                                                              @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SORT_BY_FIELD, required = false) String sortBy,
                                                              @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SORT_DIRECTION, required = false) String sortDirection){
        PostResponse postResponse = postService.getAllPosts(pageNumber, pageSize, sortBy, sortDirection);
        return ResponseEntity.ok(postResponse);
    }

    @Operation(summary = "Get Post REST API")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 success"
    )
    @GetMapping("/{id}")
    public  ResponseEntity<PostDto> getPostById(@PathVariable Long id){
        PostDto postDto = postService.getPostById(id);
        return ResponseEntity.ok(postDto);
    }

    @Operation(summary = "Get All Post by Category id REST API")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 success"
    )

    @GetMapping("/category/{id}")
    public  ResponseEntity<Set<PostDto>> getPostByCategoryId(@PathVariable("id") Long categoryId){
        Set<PostDto> postDtoSet = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtoSet);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @Operation(summary = "Update Post REST API")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 success"
    )
    @PutMapping("/{id}")
    public  ResponseEntity<PostDto> updatePost(@PathVariable Long id,  @RequestBody @Valid PostDto postDto){
        PostDto updatedPostDto = postService.updatePost(id, postDto);
        return ResponseEntity.ok(updatedPostDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer authentication"
    )
    @Operation(summary = "Delete Post REST API")
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 success"
    )
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deletePost(@PathVariable Long id){
        postService.deletePostById(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
