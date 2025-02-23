package com.blogApplication.app.services.implement;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogApplication.app.entities.CommentSection;
import com.blogApplication.app.entities.Post;
import com.blogApplication.app.exceptions.ResourceNotFoundException;
import com.blogApplication.app.payloads.CommentDao;
import com.blogApplication.app.repositories.CommentRepo;
import com.blogApplication.app.repositories.PostRepo;
import com.blogApplication.app.services.CommentService;


@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private CommentRepo commentRepo;
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDao createComment(CommentDao commentDao, Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "post id" , postId));
		CommentSection comment = this.modelMapper.map(commentDao, CommentSection.class);
		comment.setPost(post);
		CommentSection  savedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(savedComment, CommentDao.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		
		CommentSection delCom = this.commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id" , commentId));
		
		this.commentRepo.delete(delCom);
	}

}
