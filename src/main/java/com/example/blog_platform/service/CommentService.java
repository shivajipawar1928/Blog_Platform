package com.example.blog_platform.service;

import com.example.blog_platform.model.Comment;
import com.example.blog_platform.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // Get all comments for a blog post, ordered by timestamp (oldest first)
    public List<Comment> getCommentsByBlogPost(Long blogPostId) {
        return commentRepository.findByBlogPostIdOrderByTimestampAsc(blogPostId);
    }

    // Add a new comment to a blog post
    public Comment addComment(Comment comment) {
        // Here you could associate the comment with a user (if needed)
        return commentRepository.save(comment);
    }

    // Delete a comment by its ID
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
