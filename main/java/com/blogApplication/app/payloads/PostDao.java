package com.blogApplication.app.payloads;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDao {

	
	private Integer postId;
	@NotBlank
    @Size(min=6 ,message="post title must contain min 6 characters")
	private String title;
	
	@NotBlank
    @Size(min=10 ,message="Category description must contain min 10 characters")
	private String content;
	
	private String imageName;
	
	private Date addedDate;
	
	private CategoryDao category;
	
	private UserDao user;
	
	private Set<CommentDao> comments = new HashSet<>();
}
