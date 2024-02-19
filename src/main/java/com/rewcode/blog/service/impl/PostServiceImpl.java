package com.rewcode.blog.service.impl;

import com.rewcode.blog.entity.Post;
import com.rewcode.blog.exception.ResourceNotFoundException;
import com.rewcode.blog.mapper.PostMapper;
import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.payload.PostResponse;
import com.rewcode.blog.repository.PostRepository;
import com.rewcode.blog.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = postMapper.convertToPost(postDto);
        Post savedPost = postRepository.save(post);

        return  postMapper.converToPostDto(savedPost);
    }

    @Override
    public PostResponse getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        //Create Pageable instance
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = postRepository.findAll(pageable);
        //Get content from page object
        List<Post> postList = pagePost.getContent();

        List<PostDto> postDtoList = postList.stream().map(post -> postMapper.converToPostDto(post)).collect(Collectors.toList());

        PostResponse postResponse = PostResponse.builder()
                .content(postDtoList)
                .pageSize(pageSize)
                .pageNo(pageNumber)
                .totalElements(pagePost.getSize())
                .totalPages(pagePost.getTotalPages())
                .last(pagePost.isLast())
                .build();

        return postResponse;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        return postMapper.converToPostDto(post);
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);

        return postMapper.converToPostDto(updatedPost);
    }

    @Override
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }
}
