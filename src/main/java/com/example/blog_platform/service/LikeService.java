package com.example.blog_platform.service;

import com.example.blog_platform.model.Like;
import com.example.blog_platform.model.User;
import com.example.blog_platform.repository.BlogPostRepository;
import com.example.blog_platform.repository.LikeRepository;
import com.example.blog_platform.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BlogPostRepository blogPostRepository;

    // Add a like to a blog post
    public Like toggleLike(Long blogPostId, Long userId) {
        Like existingLike = likeRepository.findByUserIdAndBlogPostId(userId, blogPostId);
        
        if (existingLike != null) {
            likeRepository.delete(existingLike);
            return null; // Returning null to indicate that the like was removed
        } else {
            Like newLike = new Like();
            newLike.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            newLike.setBlogPost(blogPostRepository.findById(blogPostId).orElseThrow(() -> new RuntimeException("BlogPost not found")));
            return likeRepository.save(newLike);
        }
    }


    // Remove a like from a blog post
    public void removeLike(Long blogPostId, Long userId) {
        Like like = likeRepository.findAll().stream()
            .filter(l -> l.getBlogPost().getId().equals(blogPostId) && l.getUser().getId().equals(userId))
            .findFirst()
            .orElse(null);
        
        if (like != null) {
            likeRepository.delete(like);
        }
    }

    // Count total likes for a blog post
    public Long getLikeCount(Long blogPostId) {
        return likeRepository.countByBlogPostId(blogPostId);
    }
}
