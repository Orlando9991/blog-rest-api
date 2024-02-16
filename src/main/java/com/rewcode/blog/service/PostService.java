package com.rewcode.blog.service;


import com.rewcode.blog.entity.Post;
import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.payload.PostResponse;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDirection);

    PostDto getPostById(Long id);

    PostDto updatePost(Long id, PostDto postDto);

    void deletePostById(Long id);
}
