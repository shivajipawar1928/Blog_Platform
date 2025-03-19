package com.example.blog_platform.service;

import com.example.blog_platform.model.BlogPost;
import com.example.blog_platform.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    @Autowired
    private BlogPostRepository blogPostRepository;

    // Get all blog posts
    public Page<BlogPost> getAllBlogPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable);
    }
    
    public List<BlogPost> searchBlogPosts(String keyword) {
        return blogPostRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }
    
    public Optional<BlogPost> getBlogPostById(long id) {
        return blogPostRepository.findById(id);
    }

    // Create a new blog post
    public BlogPost createBlogPost(BlogPost blogPost) {
        // You can add any additional logic here (e.g., set the user as the creator)
        return blogPostRepository.save(blogPost);
    }

    // Update an existing blog post
    public BlogPost updateBlogPost(Long id, BlogPost blogPost) {
        // Ensure the blog post exists before updating
        Optional<BlogPost> existingBlogPost = blogPostRepository.findById(id);
        if (existingBlogPost.isPresent()) {
            BlogPost postToUpdate = existingBlogPost.get();
            postToUpdate.setTitle(blogPost.getTitle());
            postToUpdate.setContent(blogPost.getContent());
            postToUpdate.setTimestamp(blogPost.getTimestamp());
            return blogPostRepository.save(postToUpdate);
        } else {
            throw new RuntimeException("BlogPost with ID " + id + " not found");
        }
    }

    // Delete a blog post
    public void deleteBlogPost(Long id) {
        blogPostRepository.deleteById(id);
    }
}
