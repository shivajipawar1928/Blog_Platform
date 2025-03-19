package com.example.blog_platform.service;

import com.example.blog_platform.JwtUtil;
import com.example.blog_platform.dto.LoginRequest;
import com.example.blog_platform.dto.LoginResponse;
import com.example.blog_platform.model.User;
import com.example.blog_platform.repository.UserRepository;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register User
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        userRepository.save(user); // Save user in DB
    }

    // Login User and generate JWT token
    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            return new LoginResponse(token); // Return JWT token on successful login
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
    
 // Method to get current user details based on JWT token
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username);
    }

    // Method to update user profile (username, email, password)
    public User updateProfile(User user, String username) {
        User existingUser = userRepository.findByUsername(username);
        
        if (existingUser == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        
        // Update fields
        existingUser.setEmail(user.getEmail());
        existingUser.setUsername(user.getUsername());
        
        return userRepository.save(existingUser);
    }
}
    

