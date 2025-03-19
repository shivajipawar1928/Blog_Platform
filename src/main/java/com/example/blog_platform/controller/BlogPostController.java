package com.example.blog_platform.controller;

import com.example.blog_platform.model.BlogPost;
import com.example.blog_platform.model.Comment;
import com.example.blog_platform.model.Like;
import com.example.blog_platform.model.User;
import com.example.blog_platform.repository.BlogPostRepository;
import com.example.blog_platform.repository.CommentRepository;
import com.example.blog_platform.repository.LikeRepository;
import com.example.blog_platform.repository.UserRepository;
import com.example.blog_platform.service.BlogPostService;


import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/blogposts")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;
    
    @Autowired
    private BlogPostRepository blogPostRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    // Get all blog posts (only authenticated users)
    @GetMapping
    public ResponseEntity<Page<BlogPost>> getAllBlogPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<BlogPost> blogPosts = blogPostService.getAllBlogPosts(pageable);

        return ResponseEntity.ok(blogPosts);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<BlogPost>> searchBlogPosts(@RequestParam String keyword) {

        List<BlogPost> blogPosts = blogPostService.searchBlogPosts(keyword);

        return ResponseEntity.ok(blogPosts);
    }
    
    @GetMapping("/{id}")
    public Optional<BlogPost> getBlogPostById(@PathVariable("id") long id) {
        return blogPostService.getBlogPostById(id);
    }

    // Create a new blog post (only authenticated users)
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public BlogPost createBlogPost(@Valid @RequestBody BlogPost blogPost) {
        return blogPostService.createBlogPost(blogPost);
    }

    // Update a blog post (only authenticated users)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateBlogPost(@PathVariable Long id, @Valid @RequestBody BlogPost updatedPost, Principal principal) {
        BlogPost existingPost = blogPostRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User currentUser = Optional.ofNullable(userRepository.findByUsername(principal.getName()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!existingPost.getAuthor().getId().equals(currentUser.getId())) {
        	 Map<String, String> error =new HashMap<String, String>();
             error.put("message", "You can only edit your own posts.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        blogPostRepository.save(existingPost);

        return ResponseEntity.ok(existingPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> deleteBlogPost(@PathVariable Long id, Principal principal) {
        BlogPost existingPost = blogPostRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        User currentUser = Optional.ofNullable(userRepository.findByUsername(principal.getName()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!existingPost.getAuthor().getId().equals(currentUser.getId())) {
        	Map<String, String> error =new HashMap<String, String>();
        	error.put("message", "You can only delete your own posts.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }
        
        List<Like> listOfLike =
        		this.likeRepository.findByBlogPostId(existingPost.getId());
        this.likeRepository.deleteAll(listOfLike);
        
        List<Comment> listOfComment =
        		this.commentRepository.findByBlogPostId(existingPost.getId());
        this.commentRepository.deleteAll(listOfComment);
        
        this.blogPostRepository.delete(existingPost);
        Map<String, String> response =new HashMap<String, String>();
        response.put("message", "Post deleted successfully.");
        return ResponseEntity.ok(response);
    }

}
