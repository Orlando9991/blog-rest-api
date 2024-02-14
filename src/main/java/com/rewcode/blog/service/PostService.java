package com.rewcode.blog.service;


import com.rewcode.blog.payload.PostDto;

public interface PostService {
    PostDto createPost(PostDto postDto);
}
