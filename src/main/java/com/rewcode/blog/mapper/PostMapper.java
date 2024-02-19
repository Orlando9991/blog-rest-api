package com.rewcode.blog.mapper;

import com.rewcode.blog.payload.PostDto;
import com.rewcode.blog.entity.Post;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    @Autowired
    private ModelMapper modelMapper;
    public Post convertToPost(PostDto postDto){
        return modelMapper.map(postDto, Post.class);
    }

    public PostDto converToPostDto(Post post){
        return modelMapper.map(post, PostDto.class);
    }
}
