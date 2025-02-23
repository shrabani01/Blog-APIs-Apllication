package com.blogApplication.app.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogApplication.app.payloads.ApiResponse;
import com.blogApplication.app.payloads.UserDao;
import com.blogApplication.app.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	//post:create user
	@PostMapping("/")
	public ResponseEntity<UserDao> createUser(@Valid @RequestBody UserDao userDao){
		
		UserDao createUserDao = this.userService.createUser(userDao);
		
		return new ResponseEntity<>(createUserDao , HttpStatus.CREATED);
	}
	
	//put:update user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDao> updateUser(@Valid @RequestBody UserDao userDao , @PathVariable Integer userId){
		UserDao updateUser = this.userService.updateUser(userDao, userId);
		return  ResponseEntity.ok(updateUser);
	}
	
	
	//Admin 
	//delete:Delete User
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer userId){
		this.userService.deleteUser(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted Succefully" , true), HttpStatus.OK);
	}

	
	//Get:Read User
	@GetMapping("/")
	public ResponseEntity<List<UserDao>> getAllUsers(){
		
		 List<UserDao> users = userService.getAllUsers();
	        return ResponseEntity.ok(users);	
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDao> getSingleUser(@PathVariable Integer userId ){
		UserDao singleUser = userService.getUserById(userId);
		return ResponseEntity.ok(singleUser);
	}

}
