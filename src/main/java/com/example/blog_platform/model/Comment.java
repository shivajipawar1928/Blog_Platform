package com.example.blog_platform.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String content;
    
    @CreationTimestamp
    @DateTimeFormat(pattern="yyyy/MM/dd hh:mm:ss a")
    private LocalDateTime timestamp;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
    
    @ManyToOne
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

	public Comment() {
		super();
	}

	public Comment(Long id, String content, LocalDateTime timestamp, User author, BlogPost blogPost) {
		super();
		this.id = id;
		this.content = content;
		this.timestamp = timestamp;
		this.author = author;
		this.blogPost = blogPost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public BlogPost getBlogPost() {
		return blogPost;
	}

	public void setBlogPost(BlogPost blogPost) {
		this.blogPost = blogPost;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", timestamp=" + timestamp + ", author=" + author
				+ ", blogPost=" + blogPost + "]";
	}
	
	
    
    
}
