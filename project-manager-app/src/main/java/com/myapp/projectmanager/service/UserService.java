package com.myapp.projectmanager.service;

import java.util.Set;

import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;

public interface UserService {

	Set<User> findAllUsers() throws ProjectManagerServiceException;

	User createUser(User user) throws ProjectManagerServiceException;

	User updateUser(User user) throws ProjectManagerServiceException;

	User getUserById(Long userId) throws ProjectManagerServiceException;

	void deleteUserById(Long userId) throws ProjectManagerServiceException;

}
