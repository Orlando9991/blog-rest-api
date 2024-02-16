package com.rewcode.blog.service;

import com.rewcode.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsByPostId(Long postId);
    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto createComment(Long postId, CommentDto commentDto);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto);

    void deleteComment(Long postId, Long commentId);
}
