package com.example.blog_platform.controller;

import com.example.blog_platform.model.Like;
import com.example.blog_platform.model.User;
import com.example.blog_platform.repository.UserRepository;
import com.example.blog_platform.service.LikeService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(origins = "http://localhost:4200")
public class LikeController {

    @Autowired
    private LikeService likeService;
    
    @Autowired
    private UserRepository userRepository;

    // Add a like to a blog post (only authenticated users)
    // This method is updated with toggleLike()
    @PostMapping("/toggle")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> toggleLike(@RequestParam Long blogPostId, Principal principal) {
        User user = Optional.ofNullable(userRepository.findByUsername(principal.getName()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Like like = likeService.toggleLike(blogPostId, user.getId());
        
        Map<String, String> response = new HashMap<>();
        response.put("message", like == null ? "Unlike successful" : "Like successful");
        
        return ResponseEntity.ok(response);
    }


    // Get the total likes for a blog post (public access)
    @GetMapping("/blogpost/{blogPostId}")
    public Long getLikesForBlogPost(@PathVariable Long blogPostId) {
        return likeService.getLikeCount(blogPostId);  // Updated to match the service method
    }
}
