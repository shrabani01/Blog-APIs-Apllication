package com.blogApplication.app.services.implement;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogApplication.app.configuration.AppConstants;
import com.blogApplication.app.entities.Role;
import com.blogApplication.app.entities.User;
import com.blogApplication.app.payloads.UserDao;
import com.blogApplication.app.repositories.RoleRepo;
import com.blogApplication.app.repositories.UserRepo;
import com.blogApplication.app.services.UserService;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public UserDao createUser(UserDao userDao) {
            User user = this.daoToUser(userDao);
            User savedUser = this.userRepo.save(user);
		
		return this.userToDao(savedUser);
	}

	@Override
	public UserDao updateUser(UserDao userDao, Integer userId) {
		// TODO Auto-generated method stub
		   User existingUser = this.userRepo.findById(userId)
                   .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

             existingUser.setName(userDao.getName());
             existingUser.setEmail(userDao.getEmail());
             existingUser.setPassword(userDao.getPassword());
             existingUser.setAbout(userDao.getAbout());

User updatedUser = this.userRepo.save(existingUser);
return this.userToDao(updatedUser);
	}

	@Override
	public UserDao getUserById(Integer userId) {
		// TODO Auto-generated method stub
		 User user = this.userRepo.findById(userId)
                 .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
   return this.userToDao(user);
	}

	@Override
	public List<UserDao> getAllUsers() {
		// TODO Auto-generated method stub
		 List<User> users = this.userRepo.findAll();
		    return users.stream().map(this::userToDao).toList();
	}

	@Override
	public void deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		  User user = this.userRepo.findById(userId)
                  .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    this.userRepo.delete(user);
	}
	
	private User daoToUser(UserDao userDao) {
		User user =this.modelMapper.map(userDao, User.class);
//		User user = new User();
//		user.setId(userDao.getId());
//		user.setName(userDao.getName());
//		user.setEmail(userDao.getEmail());
//		user.setPassword(userDao.getPassword());
//		user.setAbout(userDao.getAbout());
		return user;
	}
	private UserDao userToDao(User user) {
		UserDao userDao = this.modelMapper.map(user, UserDao.class);
//		UserDao userDao = new UserDao();
//		userDao.setId(user.getId());
//		userDao.setName(user.getName());
//		userDao.setEmail(user.getEmail());
//		userDao.setPassword(user.getPassword());
//		userDao.setAbout(user.getAbout());
		return userDao;
	}

	@Override
	public UserDao registerNewUser(UserDao userDao) {
		User user = this.modelMapper.map(userDao, User.class);
		
		//encoded the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		
		//role
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		user.getRoles().add(role);
		User newUser = this.userRepo.save(user);
		return 	this.modelMapper.map(newUser,UserDao.class);
	}



}
