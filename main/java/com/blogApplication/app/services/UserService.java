package com.blogApplication.app.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blogApplication.app.payloads.UserDao;
@Service
public interface UserService {
	
UserDao registerNewUser(UserDao userDao);	
 UserDao createUser(UserDao userDao);
 UserDao updateUser(UserDao user , Integer userId);
 UserDao getUserById(Integer userId);
 List<UserDao> getAllUsers();
 void deleteUser(Integer userId);

}
