package com.rewcode.blog.repository;

import com.rewcode.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPostId(Long postId);

    Optional<Comment> findByPostIdAndId(Long postId, Long id);

}
