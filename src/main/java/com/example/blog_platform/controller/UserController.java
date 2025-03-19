package com.example.blog_platform.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog_platform.JwtUtil;
import com.example.blog_platform.model.User;
import com.example.blog_platform.service.UserService;

@RestController
@RequestMapping("/api/users")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Method to get the current user's profile
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
    	// Extract the username from the JWT token
        String jwtToken = token.substring(7);  // Remove "Bearer " prefix
        String username = jwtUtil.extractUsername(jwtToken);
        User currentUser = this.userService.getCurrentUser(username);
    	return ResponseEntity.ok(currentUser);
    }


    // Method to update user profile
    @PutMapping("/profile")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Map<String, String>> updateProfile(@Valid @RequestBody User user, 
                                                             @RequestHeader("Authorization") String token) {
        // Extract the current username from the JWT token
        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        String currentUsername = jwtUtil.extractUsername(jwtToken);
        
        // Update user profile
        User updatedUser = userService.updateProfile(user, currentUsername);

        // Generate a new JWT token with the updated username
        String newToken = jwtUtil.generateToken(updatedUser.getUsername());

        // Return updated user details + new token
        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile updated successfully");
        response.put("token", newToken);

        return ResponseEntity.ok(response);
    }

}
