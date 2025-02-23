package com.blogApplication.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogApplication.app.payloads.ApiResponse;
import com.blogApplication.app.payloads.CommentDao;
import com.blogApplication.app.services.CommentService;

@RestController
@RequestMapping("/api/")
public class CommentController {

	@Autowired
	private CommentService commentService;
	
	
	//create comment
	@PostMapping("/post/{postId}/comment")
	public ResponseEntity<CommentDao> createComment(@RequestBody CommentDao commentDao , @PathVariable Integer postId){
		
		CommentDao createComment = this.commentService.createComment(commentDao, postId);
		return new ResponseEntity<CommentDao>(createComment,HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
		
	  this.commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Commnet deleted succefully" , true),HttpStatus.OK);
	}
}
