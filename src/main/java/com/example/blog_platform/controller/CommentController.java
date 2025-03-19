package com.example.blog_platform.controller;

import com.example.blog_platform.JwtUtil;
import com.example.blog_platform.model.Comment;
import com.example.blog_platform.model.User;
import com.example.blog_platform.repository.CommentRepository;
import com.example.blog_platform.repository.UserRepository;
import com.example.blog_platform.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private JwtUtil jwtUtil; 
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    // Get comments for a specific blog post (no authentication required)
    @GetMapping("/blogpost/{blogPostId}")
    public List<Comment> getCommentsByBlogPost(@PathVariable Long blogPostId) {
        return commentService.getCommentsByBlogPost(blogPostId);
    }

    // Add a new comment (only authenticated users)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Comment> addComment(
    		@Valid @RequestBody Comment comment,
    		@RequestHeader("Authorization") String token) {
    	// Extract username from JWT
        String username = jwtUtil.extractUsername(token.substring(7)); // Remove "Bearer "
        User user = Optional.ofNullable(userRepository.findByUsername(username))
        	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Set the actual authenticated user
        comment.setAuthor(user);

        // Save the comment
        Comment savedComment = commentService.addComment(comment);

        return ResponseEntity.ok(savedComment);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment updatedComment, Principal principal) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        User user = Optional.ofNullable(userRepository.findByUsername(principal.getName()))
        	    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit your own comments");
        }

        comment.setContent(updatedComment.getContent());
        commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }


    // Delete a comment (only authenticated users)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
