package com.rewcode.blog.mapper;

import com.rewcode.blog.entity.Comment;
import com.rewcode.blog.entity.Post;
import com.rewcode.blog.payload.CommentDto;

public class CommentMapper {
    public static Comment convertToComment(CommentDto commentDto, Post post){
        return new Comment(
                commentDto.getId(),
                commentDto.getName(),
                commentDto.getEmail(),
                commentDto.getBody(),
                post
        );
    }

    public static CommentDto convertToCommentDto(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getName(),
                comment.getEmail(),
                comment.getBody()
        );
    }

}
