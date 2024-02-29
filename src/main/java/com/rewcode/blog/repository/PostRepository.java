package com.rewcode.blog.repository;

import com.rewcode.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    Set<Post> findByCategoryId(Long categoryId);
}
