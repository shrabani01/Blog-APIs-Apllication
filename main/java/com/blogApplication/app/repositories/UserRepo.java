package com.blogApplication.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogApplication.app.entities.User;

public interface UserRepo extends JpaRepository<User,Integer> {

	Optional<User> findByEmail(String email);
}
