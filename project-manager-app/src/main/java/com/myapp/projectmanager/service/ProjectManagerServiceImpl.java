package com.myapp.projectmanager.service;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.repository.ProjectManagerRepository;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectManagerServiceImpl.class);

	@Autowired
	private ProjectManagerRepository repository;

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
				entity.setUser(project.getUser());
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

			repository.delete(project);

		} catch (Exception e) {
			logger.error("ProjectManagerServiceException e {}", e);
			throw new ProjectManagerServiceException(
					("Exception occured while deleting project with id :" + projectId + " -- " + e.getMessage()), e);
		}

	}

}
