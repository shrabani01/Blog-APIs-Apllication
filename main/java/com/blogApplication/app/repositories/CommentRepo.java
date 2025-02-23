package com.blogApplication.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogApplication.app.entities.CommentSection;

public interface CommentRepo extends JpaRepository<CommentSection, Integer> {
	

}
