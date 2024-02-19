package com.rewcode.blog.controller;

import com.rewcode.blog.payload.CommentDto;
import com.rewcode.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComentsByPostId(@PathVariable Long postId){
        List<CommentDto> commentDtosList = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(commentDtosList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComentById(@PathVariable Long postId, @PathVariable("id") Long commentId){
        CommentDto commentDto = commentService.getCommentById(postId, commentId);
        return ResponseEntity.ok(commentDto);
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto){
        CommentDto savedCommentId = commentService.createComment(postId, commentDto);
        return new ResponseEntity(savedCommentId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId,
                                                        @PathVariable("id") Long commentId,
                                                        @RequestBody CommentDto commentDto ){
        CommentDto updatedCommentDto = commentService.updateComment(postId, commentId, commentDto);
        return ResponseEntity.ok(updatedCommentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId,
                                                @PathVariable("id") Long commentId){
        commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok(String.format("Comment with id: %s that belongs to Post with id: %s deleted", commentId, postId));
    }


}
