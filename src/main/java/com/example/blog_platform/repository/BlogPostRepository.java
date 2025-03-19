package com.example.blog_platform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_platform.model.BlogPost;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
	Page<BlogPost> findAll(Pageable pageable);
	
	List<BlogPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);

}
