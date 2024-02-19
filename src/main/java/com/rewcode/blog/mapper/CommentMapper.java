package com.rewcode.blog.mapper;

import com.rewcode.blog.entity.Comment;
import com.rewcode.blog.entity.Post;
import com.rewcode.blog.payload.CommentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    @Autowired
    private ModelMapper modelMapper;
    public Comment convertToComment(CommentDto commentDto, Post post){
        Comment convertedComment = modelMapper.map(commentDto, Comment.class);
        convertedComment.setPost(post);
        return convertedComment;
    }

    public CommentDto convertToCommentDto(Comment comment){
        return modelMapper.map(comment, CommentDto.class);
    }

}
