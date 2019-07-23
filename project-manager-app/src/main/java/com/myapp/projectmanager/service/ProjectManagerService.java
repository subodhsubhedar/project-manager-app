package com.myapp.projectmanager.service;

import java.util.Set;

import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;

public interface ProjectManagerService {

	Set<Project> findAllProjects() throws ProjectManagerServiceException;

	Project createProject(Project project) throws ProjectManagerServiceException;

	Project updateProject(Project project) throws ProjectManagerServiceException;

	Project getProjectById(Long projectId) throws ProjectManagerServiceException;

	void deleteProjectById(Long projectId) throws ProjectManagerServiceException;

}
