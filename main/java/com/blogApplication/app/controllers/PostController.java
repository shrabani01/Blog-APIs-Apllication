package com.blogApplication.app.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blogApplication.app.configuration.AppConstants;
import com.blogApplication.app.payloads.ApiResponse;
import com.blogApplication.app.payloads.PostDao;
import com.blogApplication.app.payloads.PostResponse;
import com.blogApplication.app.services.FileService;
import com.blogApplication.app.services.PostService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/")
public class PostController {

	@Autowired
	PostService postService;
	
	
	@Autowired
	private FileService fileService;
	
	
	@Value("${project.image}")
	private String path;
	//create
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDao> createPost(@Valid @RequestBody PostDao postDao ,@PathVariable Integer userId,@PathVariable Integer categoryId){
		
		PostDao createPost = this.postService.createPost(postDao, categoryId, userId);
		return new ResponseEntity<PostDao>(createPost,HttpStatus.CREATED);
	}
	
	//get posts by user
	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber",defaultValue="0",required=false)Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue="10", required=false)Integer pageSize
			){
		PostResponse posts = this.postService.getPostsByUser(userId,pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(posts , HttpStatus.OK);
	}
	
	//get posts by category
		@GetMapping("/category/{categoryId}/posts")
		public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
				@RequestParam(value = "pageNumber",defaultValue="0",required=false)Integer pageNumber,
				@RequestParam(value = "pageSize", defaultValue="10", required=false)Integer pageSize
				){
			PostResponse posts = this.postService.getPostsByCategory(categoryId, pageNumber, pageSize);
			return new ResponseEntity<PostResponse>(posts , HttpStatus.OK);
		}
		
		
		//get all posts
		@GetMapping("/posts")
		public ResponseEntity<PostResponse> getAllPosts(
				@RequestParam(value = "pageNumber",defaultValue=AppConstants.PAGE_NUMBER,required=false)Integer pageNumber,
				@RequestParam(value = "pageSize", defaultValue=AppConstants.PAGE_SIZE, required=false)Integer pageSize,
				@RequestParam(value = "sortBy", defaultValue=AppConstants.SORT_BY, required=false)String sortBy,
				@RequestParam(value = "sortDir", defaultValue=AppConstants.SORT_DIR, required=false)String sortDir


				){
			PostResponse postResponse = this.postService.getAllPosts(pageNumber , pageSize,sortBy, sortDir);
			return new ResponseEntity<PostResponse>(postResponse,HttpStatus.OK);
		}
		
		
		//get posts by id
		@GetMapping("/posts/{postId}")
		public ResponseEntity<PostDao> getPostById(@PathVariable Integer postId){
			PostDao postById = this.postService.getPostById(postId);
			return new ResponseEntity<PostDao>(postById,HttpStatus.OK);
		}
		
		
		//delete post
		@DeleteMapping("/posts/{postId}")
		public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId){
			this.postService.deletePost(postId);
			return new ResponseEntity<ApiResponse>(new ApiResponse("Post is deleted Succefully" , true), HttpStatus.OK);
		}

		//update post
		@PutMapping("/posts/{postId}")
		public ResponseEntity<PostDao> updatePost( @RequestBody PostDao postDao ,@PathVariable Integer postId){
			PostDao updatePost = this.postService.updatePost( postDao,postId);
			return new ResponseEntity<PostDao>(updatePost, HttpStatus.OK);
		}
		
		
		//search posts
		@GetMapping("/posts/search/{keywords}")
		public ResponseEntity<List<PostDao>> searchPostByTitle(@PathVariable("keywords") String keywords){
			List<PostDao> searchPosts = this.postService.searchPosts(keywords);
			return new ResponseEntity<List<PostDao>>(searchPosts,HttpStatus.OK);
		}
		
		
		@PostMapping("/post/image/upload/{postId}")
		public ResponseEntity<?> uploadPostImage(@RequestParam("image") MultipartFile image, 
		                                         @PathVariable Integer postId) {
		    try {
		        // Validate input
		        if (image == null || image.isEmpty()) {
		            return new ResponseEntity<>("Image file is required", HttpStatus.BAD_REQUEST);
		        }

		        // Fetch post by ID
		        PostDao postDaos = this.postService.getPostById(postId);
		        if (postDaos == null) {
		            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
		        }

		        // Upload image
		        String fileName = this.fileService.uploadImage(path, image);

		        // Update post with image name
		        postDaos.setImageName(fileName);
		        PostDao updatedPost = this.postService.updatePost(postDaos, postId);

		        // Return updated post
		        return new ResponseEntity<>(updatedPost, HttpStatus.OK);

		    } catch (IOException e) {
		        // Log and return error
		        e.printStackTrace();
		        return new ResponseEntity<>("Failed to upload image: " + e.getMessage(), 
		                                    HttpStatus.INTERNAL_SERVER_ERROR);
		    } catch (Exception e) {
		        // Handle other unexpected errors
		        e.printStackTrace();
		        return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), 
		                                    HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		}

		
		@GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
		public void downloadIamge(@PathVariable("imageName") String imageName , HttpServletResponse response) throws IOException {
			InputStream resource = this.fileService.getResource(path, imageName);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(resource,response.getOutputStream());
			
		}
}
