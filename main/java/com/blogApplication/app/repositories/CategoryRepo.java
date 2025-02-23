package com.blogApplication.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogApplication.app.entities.Category;

public interface CategoryRepo extends JpaRepository<Category,Integer> {

}
