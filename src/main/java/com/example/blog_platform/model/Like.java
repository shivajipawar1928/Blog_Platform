package com.example.blog_platform.model;


import javax.persistence.*;

@Entity
@Table(name = "post_likes")  // Renaming to avoid SQL conflict
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "blog_post_id", nullable = false)
    private BlogPost blogPost;

    
    
	public Like() {
		super();
	}

	public Like(Long id, User user, BlogPost blogPost) {
		super();
		this.id = id;
		this.user = user;
		this.blogPost = blogPost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BlogPost getBlogPost() {
		return blogPost;
	}

	public void setBlogPost(BlogPost blogPost) {
		this.blogPost = blogPost;
	}

	@Override
	public String toString() {
		return "Like [id=" + id + ", user=" + user + ", blogPost=" + blogPost + "]";
	}
    
}
