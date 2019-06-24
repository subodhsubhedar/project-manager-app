package com.myapp.projectmanager.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.repository.UserRepository;

public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository repository;

	@Override
	public Set<User> findAllUsers() throws ProjectManagerServiceException {
		logger.debug("Calling repository for findAllUsers");
		try {
			return new LinkedHashSet<>(repository.findAll());

		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					"Exception Occured while retrieving all Users." + " -- " + e.getMessage(), e);
		}
	}

	@Override
	public User createUser(User user) throws ProjectManagerServiceException {
		logger.debug("Calling repository for createUser User {}", user);
		try {
			return repository.save(user);

		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while creating user :" + user + " -- " + e.getMessage()), e);
		}
	}

	@Override
	public User updateUser(User user) throws ProjectManagerServiceException {
		logger.debug("Calling repository for updateUser user {}", user);
		try {

			User entity = getUserById(user.getUserId());

			entity.setEmpId(user.getEmpId());
			entity.setFirstName(user.getFirstName());
			entity.setLastName(user.getLastName());

			if (user.getTask() != null) {
				entity.setTask(user.getTask());
			}

			if (user.getProject() != null) {
				entity.setProject(user.getProject());
			}

			return repository.save(entity);
		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while updating Task :" + user.getUserId() + " -- " + e.getMessage()), e);
		}

	}

	@Override
	public User getUserById(Long userId) throws ProjectManagerServiceException {
		logger.debug("Calling repository for getUserById userId {}", userId);
		User user = null;
		try {

			Optional<User> optional = repository.findById(userId);

			if (optional.isPresent()) {
				user = optional.get();
			} else {
				logger.debug("getUserById user not found");
				throw new ProjectManagerServiceException("User Not found with id :" + userId);
			}
			return user;
		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while retrieving Task with id :" + userId + " -- " + e.getMessage()), e);
		}
	}

	@Override
	public void deleteUserById(Long userId) throws ProjectManagerServiceException {
		logger.debug("Calling repository for deleteUserById taskId {}", userId);
		try {
			User user = null;

			Optional<User> optional = repository.findById(userId);

			if (optional.isPresent()) {
				user = optional.get();
			} else {
				logger.debug("getTaskById task not found");
				throw new ProjectManagerServiceException("Task Not found with id :" + userId);
			}

			repository.delete(user);

		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while deleting task with id :" + userId + " -- " + e.getMessage()), e);
		}

	}

}
