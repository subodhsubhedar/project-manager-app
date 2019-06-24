package com.myapp.projectmanager.mapper;

import java.util.Set;

import com.myapp.projectmanager.dto.ParentTaskDTO;
import com.myapp.projectmanager.dto.ProjectDTO;
import com.myapp.projectmanager.dto.TaskDTO;
import com.myapp.projectmanager.dto.UserDTO;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;

/**
 * 
 * @author Admin
 *
 */
public interface ProjectManagerEntityToDtoMapper {

	Task getMappedTaskEntity(TaskDTO taskDto);

	TaskDTO getMappedTaskDto(Task taskEntity);

	Set<TaskDTO> getMappedTaskDtoSet(Set<Task> taskEntitySet);

	Set<Task> getMappedTaskEntitySet(Set<TaskDTO> taskDtoSet);
	
	Set<ParentTaskDTO> getMappedParentTaskDtoSet(Set<ParentTask> parentTaskEntitySet);

	ParentTask getMappedParentTaskEntity(ParentTaskDTO parentTaskDto);

	ParentTaskDTO getMappedParentTaskDto(ParentTask parentTaskEntity);

	User getMappedUserEntity(UserDTO userDto);

	UserDTO getMappedUserDto(User userEntity);

	Set<UserDTO> getMappedUserDtoSet(Set<User> userEntitySet);

	Project getMappedProjectEntity(ProjectDTO projectDto);

	ProjectDTO getMappedProjectDto(Project projectEntity);

	Set<ProjectDTO> getMappedProjectDtoSet(Set<Project> projectEntitySet);

}
