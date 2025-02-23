package com.blogApplication.app.services;

import java.util.List;


import com.blogApplication.app.payloads.CategoryDao;


public interface CategoryService {

	//create
	CategoryDao createCategory(CategoryDao categoryDao);
	//update
	CategoryDao updateCategory(CategoryDao categoryDao, Integer categoryId);
	//delete
	void deleteCategory(Integer categoryId);
	//get
	CategoryDao getCategoryById(Integer categoryId);
	//getAll
	List<CategoryDao> getAllCategories();
}
