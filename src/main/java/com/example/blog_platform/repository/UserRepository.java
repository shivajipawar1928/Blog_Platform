package com.example.blog_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog_platform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
}
