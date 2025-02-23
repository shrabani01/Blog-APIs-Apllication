package com.blogApplication.app.services.implement;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blogApplication.app.entities.Category;
import com.blogApplication.app.entities.Post;
import com.blogApplication.app.entities.User;
import com.blogApplication.app.exceptions.ResourceNotFoundException;
import com.blogApplication.app.payloads.PostDao;
import com.blogApplication.app.payloads.PostResponse;
import com.blogApplication.app.repositories.CategoryRepo;
import com.blogApplication.app.repositories.PostRepo;
import com.blogApplication.app.repositories.UserRepo;
import com.blogApplication.app.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	
	@Override
	public PostDao createPost(PostDao postDao , Integer categoryId , Integer userId) {
		
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User " , "User id" ,userId));
		
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category " , "Category id" ,categoryId));
 
		
		Post post = this.modelMapper.map(postDao , Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = this.postRepo.save(post);
		
		return this.modelMapper.map(newPost , PostDao.class);
	}

	@Override
	public PostDao updatePost(PostDao postDao, Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		post.setTitle(postDao.getTitle());
		post.setContent(postDao.getContent());
		post.setImageName(postDao.getImageName());
		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDao.class);
	}

	@Override
	public void deletePost(Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber , Integer pageSize, String sortBy, String sortDir) {
		
		Sort sort = (sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending());
		Pageable p = PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> pagePost = this.postRepo.findAll(p);
		List<Post> allPosts = pagePost.getContent();
		List<PostDao> postDaos =  allPosts.stream().map((post)->this.modelMapper.map(post,PostDao.class)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDaos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getNumberOfElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		return postResponse;
	}



	@Override
	public PostDao getPostById(Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","post id",postId));
		return this.modelMapper.map(post, PostDao.class);
	}

	@Override
	public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize);

	    Category cat = this.categoryRepo.findById(categoryId)
	            .orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

	 
	    Page<Post> pagePost = this.postRepo.findByCategory(cat, pageable);

	    List<PostDao> postDaos = pagePost.getContent().stream()
	            .map(post -> this.modelMapper.map(post, PostDao.class))
	            .collect(Collectors.toList());

	    PostResponse postResponse = new PostResponse();
	    postResponse.setContent(postDaos);
	    postResponse.setPageNumber(pagePost.getNumber());
	    postResponse.setPageSize(pagePost.getSize());
	    postResponse.setTotalElements(pagePost.getTotalElements()); // Corrected to get total elements
	    postResponse.setTotalPages(pagePost.getTotalPages());
	    postResponse.setLastPage(pagePost.isLast());

	    return postResponse;
	}


	@Override
	public PostResponse getPostsByUser(Integer userId , Integer pageNumber, Integer pageSize) {
		  Pageable pageable = PageRequest.of(pageNumber, pageSize);
	    User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","user id",userId));
	    Page<Post> pagePost = this.postRepo.findByUser(user,pageable);
	    List<PostDao> postDaos = pagePost.getContent().stream().map((post)->this.modelMapper.map(post, PostDao.class)).collect(Collectors.toList());
	
	    PostResponse postResponse = new PostResponse();
	    postResponse.setContent(postDaos);
	    postResponse.setPageNumber(pagePost.getNumber());
	    postResponse.setPageSize(pagePost.getSize());
	    postResponse.setTotalElements(pagePost.getTotalElements()); // Corrected to get total elements
	    postResponse.setTotalPages(pagePost.getTotalPages());
	    postResponse.setLastPage(pagePost.isLast());

	    return postResponse;
	}

	@Override
	public List<PostDao> searchPosts(String keyword) {
		List<Post> searchPosts = this.postRepo.findByTitleContaining(keyword);
		List<PostDao> postDaos = searchPosts.stream().map((post)->this.modelMapper.map(post, PostDao.class)).collect(Collectors.toList());
		
		return postDaos;
	}

}
