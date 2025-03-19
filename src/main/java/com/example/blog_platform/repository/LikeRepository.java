package com.example.blog_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_platform.model.BlogPost;
import com.example.blog_platform.model.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByBlogPostId(Long blogPostId);  // To get the total likes for a post

	boolean existsByUserIdAndBlogPostId(Long id, Long id2);

	Like findByUserIdAndBlogPostId(Long userId, Long blogPostId);

	List<Like> findByBlogPostId(Long blogPostId);
}
