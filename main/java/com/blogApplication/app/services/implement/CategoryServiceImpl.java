package com.blogApplication.app.services.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogApplication.app.entities.Category;
import com.blogApplication.app.exceptions.ResourceNotFoundException;
import com.blogApplication.app.payloads.CategoryDao;
import com.blogApplication.app.repositories.CategoryRepo;
import com.blogApplication.app.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CategoryDao createCategory(CategoryDao categoryDao) {
		Category cat = this.modelMapper.map(categoryDao, Category.class);
		Category addedCat = this.categoryRepo.save(cat);
		return this.modelMapper.map(addedCat, CategoryDao.class);
	}

	@Override
	public CategoryDao updateCategory(CategoryDao categoryDao, Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" ,"category id" , categoryId));
		cat.setCategoryTitle( categoryDao.getCategoryTitle());
		cat.setCategoryDescription(categoryDao.getCategoryDescription());
		Category updatedCat = this.categoryRepo.save(cat);
		return this.modelMapper.map(updatedCat, CategoryDao.class);
	}

	@Override
	public void deleteCategory(Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" ,"category id" , categoryId));
		this.categoryRepo.delete(cat);
		
	}

	@Override
	public CategoryDao getCategoryById(Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category" ,"Category Id" , categoryId));

		return this.modelMapper.map(cat, CategoryDao.class);
	}

	@Override
	public List<CategoryDao> getAllCategories() {
		List<Category> categories = this.categoryRepo.findAll();
		List<CategoryDao> catDaos = categories.stream().map((cat)-> this.modelMapper.map(cat , CategoryDao.class)).collect(Collectors.toList());
		return catDaos;
	}

}
