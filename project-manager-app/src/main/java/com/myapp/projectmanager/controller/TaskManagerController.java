package com.myapp.projectmanager.controller;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.projectmanager.dto.ParentTaskDTO;
import com.myapp.projectmanager.dto.TaskDTO;
import com.myapp.projectmanager.dto.TaskDTO.UpdateTaskValidateGroup;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.mapper.ProjectManagerEntityToDtoMapper;
import com.myapp.projectmanager.service.TaskManagerService;

/**
 * 
 * @author Admin
 *
 */
@RestController
public class TaskManagerController {

	private static final Logger logger = LoggerFactory.getLogger(TaskManagerController.class);

	@Autowired
	private TaskManagerService taskMngrService;

	@Autowired
	private ProjectManagerEntityToDtoMapper mapper;

	@GetMapping(value = "/tasks")
	public Set<TaskDTO> listAllTasks() throws ProjectManagerServiceException {
		logger.debug("GET listAllTasks() request received");
		return getMappedDtoSet(taskMngrService.findAllTasks());
	}

	@GetMapping(value = "/parent-tasks")
	public Set<ParentTaskDTO> listAllParentTasks() throws ProjectManagerServiceException {
		logger.debug("GET listAllParentTasks() request received");
		return getMappedParentDtoSet(taskMngrService.findAllParenTasks());

	}

	@GetMapping(value = "/task/{taskId}")
	public TaskDTO getTask(@PathVariable @Valid @NotNull Long taskId) throws ProjectManagerServiceException {
		logger.debug("GET getTask() request received for {}", taskId);
		return getMappedDto(taskMngrService.getTaskById(taskId));
	}

	@PostMapping(value = "/task/add")
	public TaskDTO addNewTask(@Valid @RequestBody TaskDTO taskDto) throws ProjectManagerServiceException {
		logger.debug("POST addNewTask() request received {}", taskDto);

		Task persistentTask = getMappedEntity(taskDto);
		logger.debug("POST addNewTask() mapped entity {}", persistentTask);

		return getMappedDto(taskMngrService.createTask(persistentTask));
	} 

	@PutMapping(value = "/task/update")
	public TaskDTO updateTask(@Validated({ UpdateTaskValidateGroup.class }) @RequestBody TaskDTO taskDto)
			throws ProjectManagerServiceException {
		logger.debug("PUT updateTask() request received {}", taskDto);

		Task persistentTask = getMappedEntity(taskDto);
		logger.debug("PUT updateTask() mapped entity {}", persistentTask);
		return getMappedDto(taskMngrService.updateTask(persistentTask));
	}

	@DeleteMapping(value = "/task/delete/{taskId}")
	public void deleteTask(@PathVariable @Valid @NotNull Long taskId) throws ProjectManagerServiceException {
		logger.debug("DELETE deleteTask() request received {}", taskId);
		taskMngrService.deleteTaskById(taskId);
	}

	/**
	 * 
	 * @param taskDto
	 * @return
	 */
	private Task getMappedEntity(TaskDTO taskDto) {
		return mapper.getMappedTaskEntity(taskDto);
	}

	/**
	 * 
	 * @param taskEntitySet
	 * @return
	 */
	private Set<TaskDTO> getMappedDtoSet(Set<Task> taskEntitySet) {
		return mapper.getMappedTaskDtoSet(taskEntitySet);
	}

	/**
	 * 
	 * @param parentTaskEntitySet
	 * @return
	 */
	private Set<ParentTaskDTO> getMappedParentDtoSet(Set<ParentTask> parentTaskEntitySet) {
		return mapper.getMappedParentTaskDtoSet(parentTaskEntitySet);
	}

	/**
	 * 
	 * @param taskEntity
	 * @return
	 */
	private TaskDTO getMappedDto(Task taskEntity) {
		return mapper.getMappedTaskDto(taskEntity);
	}

}
