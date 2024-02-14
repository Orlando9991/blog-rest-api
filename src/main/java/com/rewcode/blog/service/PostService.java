package com.rewcode.blog.service;


import com.rewcode.blog.entity.Post;
import com.rewcode.blog.payload.PostDto;

import java.util.List;
import java.util.Optional;

public interface PostService {
    PostDto createPost(PostDto postDto);

    List<PostDto> getAllPosts();

    PostDto getPostById(Long id);

    PostDto updatePost(Long id, PostDto postDto);

    void deletePostById(Long id);
}
