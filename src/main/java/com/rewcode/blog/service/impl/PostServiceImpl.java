package com.rewcode.blog.service.impl;

import com.rewcode.blog.entity.Post;
import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.repository.PostRepository;
import com.rewcode.blog.service.PostService;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        //Convert PostDTO to Post
        Post post = new Post();
        post.setTitle(post.getTitle());
        post.setDescription(post.getDescription());
        post.setContent(post.getContent());

        Post savedPost = postRepository.save(post);

        //Convert Post to PostDTO
        PostDto postResponse = new PostDto();
        postResponse.setId(savedPost.getId());
        postResponse.setTitle(savedPost.getTitle());
        postResponse.setDescription(savedPost.getDescription());
        postResponse.setContent(savedPost.getContent());

        return postResponse;
    }
}
