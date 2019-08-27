package com.myapp.projectmanager.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.repository.ProjectManagerRepository;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectManagerServiceImpl.class);

	@Autowired
	private ProjectManagerRepository repository;

	@Autowired
	private UserService userService;

	@Override
	public Set<Project> findAllProjects() throws ProjectManagerServiceException {

		logger.debug("Calling repository for findAllProjects");
		try {
			return new LinkedHashSet<>(repository.findAll());

		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					"Exception Occured while retrieving all Projects." + " -- " + e.getMessage(), e);
		}
	}

	@Override
	public Project createProject(Project project) throws ProjectManagerServiceException {
		logger.debug("Calling repository for createProject Project {}", project);
		try {
			if (project.getUser() != null) {
				User userEntity = userService.getUserById(project.getUser().getUserId());

				userEntity.setProject(project);

				project.setUser(userEntity);
			}

			return repository.save(project);

		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while creating project :" + project + " -- " + e.getMessage()), e);
		}
	}

	@Override
	public Project updateProject(Project project) throws ProjectManagerServiceException {
		logger.debug("Calling repository for updateProject project {}", project);
		try {

			Project entity = getProjectById(project.getProjectId());

			entity.setProject(project.getProject());
			entity.setStartDate(project.getStartDate());
			entity.setEndDate(project.getEndDate());
			entity.setPriority(project.getPriority());

			if (project.getTasks() != null) {
				entity.setTasks(project.getTasks());
			}

			if (project.getUser() != null) {

				User userEntity = userService.getUserById(project.getUser().getUserId());

				userEntity.setProject(project);

				entity.setUser(userEntity);
			} else {
				if (entity.getUser() != null) {
					
					User userEnty = userService.getUserById(entity.getUser().getUserId());
					userEnty.setProject(null);
					entity.setUser(userEnty);
				}
			}

			return repository.save(entity);
		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while updating project :" + project.getProjectId() + " -- " + e.getMessage()),
					e);
		}
	}

	@Override
	public Project getProjectById(Long projectId) throws ProjectManagerServiceException {

		logger.debug("Calling repository for getProjectById getProjectById {}", projectId);
		Project project = null;
		try {

			Optional<Project> optional = repository.findById(projectId);

			if (optional.isPresent()) {
				project = optional.get();
			} else {
				logger.debug("getProjectById project not found");
				throw new ProjectManagerServiceException("Project Not found with id :" + projectId);
			}
			return project;
		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while retrieving Project with id :" + projectId + " -- " + e.getMessage()), e);
		}
	}

	@Override
	public void deleteProjectById(Long projectId) throws ProjectManagerServiceException {
		logger.debug("Calling repository for deleteProjectById projectId {}", projectId);
		try {
			Project project = null;

			Optional<Project> optional = repository.findById(projectId);

			if (optional.isPresent()) {
				project = optional.get();
			} else {
				logger.debug("findById Project not found");
				throw new ProjectManagerServiceException("Project Not found with id :" + projectId);
			} 
			
			if (project.getUser() != null) {

				User userEntity = userService.getUserById(project.getUser().getUserId());
				userEntity.setProject(null);

				project.setUser(userEntity);
			}  			
			repository.delete(project);

		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while deleting project with id :" + projectId + " -- " + e.getMessage()), e);
		}

	}

	@Override
	@Transactional
	public Project getProject(String projectDesc) throws ProjectManagerServiceException {
		logger.debug("Calling repository for getProject projectDesc {}", projectDesc);
		Project project = null;
		try {

			Optional<Project> optional = repository.findByProject(projectDesc);

			if (optional.isPresent()) {
				project = optional.get();
			} else {
				logger.debug("Project Not found {}", projectDesc);
				throw new ProjectManagerServiceException("Project Not found with desc :" + projectDesc);
			}
			return project;
		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while retrieving project with desc" + projectDesc + " -- " + e.getMessage()),
					e);
		}
	}

}
