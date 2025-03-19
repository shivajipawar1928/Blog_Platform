package com.example.blog_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_platform.model.BlogPost;
import com.example.blog_platform.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
    List<Comment> findByBlogPostIdOrderByTimestampAsc(Long blogPostId);

	List<Comment> findByBlogPostId(Long blogPostId);
}
