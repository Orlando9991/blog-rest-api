package com.rewcode.blog.service.impl;

import com.rewcode.blog.entity.Comment;
import com.rewcode.blog.entity.Post;
import com.rewcode.blog.exception.ResourceNotFoundException;
import com.rewcode.blog.mapper.CommentMapper;
import com.rewcode.blog.payload.CommentDto;
import com.rewcode.blog.repository.CommentRepository;
import com.rewcode.blog.repository.PostRepository;
import com.rewcode.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;
    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentMapper.convertToComment(commentDto, post);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.convertToCommentDto(savedComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> postList = commentRepository.findByPostId(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return postList.stream()
                .map((comment -> commentMapper.convertToCommentDto(comment)))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findByPostIdAndId(postId,commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        return commentMapper.convertToCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByPostIdAndId(postId,commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.convertToCommentDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findByPostIdAndId(postId, commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        commentRepository.delete(comment);
    }
}
