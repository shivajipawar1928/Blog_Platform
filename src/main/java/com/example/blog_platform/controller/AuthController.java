package com.example.blog_platform.controller;

import com.example.blog_platform.dto.LoginRequest;
import com.example.blog_platform.dto.LoginResponse;
import com.example.blog_platform.model.User;
import com.example.blog_platform.service.UserService;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody User user)  {
    	try {
        userService.registerUser(user);

        // Returning JSON response instead of plain string
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registration successful! Please login.");
        return ResponseEntity.ok(response);
    	}
    //	
    	catch(DataIntegrityViolationException e) {
    		Map<String, String> response = new HashMap<>();
            response.put("message", "Registration Failed: "+e.getMostSpecificCause().getLocalizedMessage());
             return ResponseEntity.ok(response);
    	}
    }
    
    // User Login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }
}
