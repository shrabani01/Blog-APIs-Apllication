package com.blogApplication.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blogApplication.app.payloads.ApiResponse;
import com.blogApplication.app.payloads.CategoryDao;
import com.blogApplication.app.services.CategoryService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	
	@Autowired
   private CategoryService categoryService;
	
	
	
	//create
	@PostMapping("/")
	public ResponseEntity<CategoryDao> createUser(@Valid @RequestBody CategoryDao categoryDao){
		
		CategoryDao createCategory = this.categoryService.createCategory(categoryDao);
		
		return new ResponseEntity<CategoryDao>(createCategory , HttpStatus.CREATED);
	}
	
	

	//update
	@PutMapping("/{catId}")
	public ResponseEntity<CategoryDao> updateUser(@Valid @RequestBody CategoryDao categoryDao , @PathVariable Integer catId){
		CategoryDao updateCategory = this.categoryService.updateCategory(categoryDao, catId);
		return  new ResponseEntity<CategoryDao>(updateCategory,HttpStatus.OK);
	}
	
   //delete
	@DeleteMapping("/{catId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId){
		this.categoryService.deleteCategory(catId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted Succefully" , true), HttpStatus.OK);
	}

	
	
	@GetMapping("/{userId}")
	public ResponseEntity<CategoryDao> getSingleUser(@PathVariable Integer catId ){
		CategoryDao singleCategory = categoryService.getCategoryById(catId);
		return ResponseEntity.ok(singleCategory);
	}
	
	
	//Get:Read categories
	@GetMapping("/")
	public ResponseEntity<List<CategoryDao>> getAllCategories(){
		
		 List<CategoryDao> categories = categoryService.getAllCategories();
	        return ResponseEntity.ok(categories);	
	}



}
