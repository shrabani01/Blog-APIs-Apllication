package com.blogApplication.app.services;

import java.util.List;

import com.blogApplication.app.payloads.PostDao;
import com.blogApplication.app.payloads.PostResponse;

public interface PostService {
	
	//create
	PostDao createPost(PostDao postDao , Integer categoryId , Integer userId);
	
	//update
	PostDao updatePost(PostDao postDao , Integer postId);
	
	//delete
	void deletePost(Integer postId);
	
	
	//get all posts
	PostResponse getAllPosts(Integer pageNumber , Integer pageSize, String sortBy, String sortDir);
	
	
	//get single post
	PostDao getPostById(Integer postId);
	
	
	//get posts by category
	PostResponse getPostsByCategory(Integer categoryId ,Integer pageNumber , Integer pageSize);
	
	
	//get posts by user
	PostResponse getPostsByUser(Integer userId , Integer pageNumber, Integer pageSize);
	
	
	//search posts
	List<PostDao> searchPosts(String keyword);
		

}
