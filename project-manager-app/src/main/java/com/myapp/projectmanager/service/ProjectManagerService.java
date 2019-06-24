package com.myapp.projectmanager.service;

import java.util.Set;

import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;

public interface ProjectManagerService {

	Set<Project> findAllProjects() throws ProjectManagerServiceException;

	Project createUser(Project project) throws ProjectManagerServiceException;

	Project updateUser(Project project) throws ProjectManagerServiceException;

	Project getUserById(Long projectId) throws ProjectManagerServiceException;

	void deletePojectById(Long projectId) throws ProjectManagerServiceException;

}
