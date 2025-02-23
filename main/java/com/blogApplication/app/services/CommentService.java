package com.blogApplication.app.services;

import com.blogApplication.app.payloads.CommentDao;

public interface CommentService {

	
	public CommentDao createComment(CommentDao commentDao , Integer postId);
	
	public void deleteComment(Integer commentId);
}
