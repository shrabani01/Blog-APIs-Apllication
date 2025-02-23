package com.blogApplication.app.repositories;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blogApplication.app.entities.Category;
import com.blogApplication.app.entities.Post;
import com.blogApplication.app.entities.User;

public interface PostRepo extends JpaRepository<Post , Integer> {
	
     Page<Post> findByUser(User user , Pageable pageable);
     Page<Post> findByCategory(Category category , Pageable pageable);
     
     List<Post> findByTitleContaining(String title);
     
}
