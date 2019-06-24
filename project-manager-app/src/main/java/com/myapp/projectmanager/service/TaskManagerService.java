package com.myapp.projectmanager.service;

import java.util.Set;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;

/**
 * 
 * @author Admin
 *
 */
public interface TaskManagerService {

	Set<Task> findAllTasks() throws ProjectManagerServiceException;
	
	Set<ParentTask> findAllParenTasks() throws ProjectManagerServiceException;

	Task createTask(Task task) throws ProjectManagerServiceException;

	Task updateTask(Task task) throws ProjectManagerServiceException;

	Task getTask(String taskDesc) throws ProjectManagerServiceException;

	Task getTaskById(Long taskId) throws ProjectManagerServiceException;
	
	ParentTask getParentTaskById(Long taskId) throws ProjectManagerServiceException;

	void deleteTaskById(Long taskId) throws ProjectManagerServiceException;

}
