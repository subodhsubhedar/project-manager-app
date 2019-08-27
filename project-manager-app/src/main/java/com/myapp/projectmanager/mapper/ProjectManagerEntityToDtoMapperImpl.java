package com.myapp.projectmanager.mapper;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

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
@Component
public class ProjectManagerEntityToDtoMapperImpl implements ProjectManagerEntityToDtoMapper {

	@Override
	public Task getMappedTaskEntity(TaskDTO taskDto) {
 
		Task target = new Task();
		BeanUtils.copyProperties(taskDto, target);

		if (taskDto.getParentTask() != null) {
			ParentTask trgt = getMappedParentTaskEntity(taskDto.getParentTask());
			target.setParentTask(trgt);
		}
		if (taskDto.getUser() != null) {

			User usrTrgt = getMappedUserEntity(taskDto.getUser());
			target.setUser(usrTrgt);
		}
 
		if (taskDto.getProjectName() != null) {
			ProjectDTO projDto = new ProjectDTO(taskDto.getProjectId(), taskDto.getProjectName(), null, null, 0);
			Project projTrgt = getMappedProjectEntity(projDto);
			target.setProject(projTrgt);
		}

		return target;
	}

	@Override
	public TaskDTO getMappedTaskDto(Task taskEntity) {

		TaskDTO target = new TaskDTO();
		BeanUtils.copyProperties(taskEntity, target);

		if (taskEntity.getParentTask() != null) {
			ParentTaskDTO trgt = getMappedParentTaskDto(taskEntity.getParentTask());
			target.setParentTask(trgt);
		}

		if (taskEntity.getUser() != null) {
			UserDTO trgt = getMappedUserDto(taskEntity.getUser());
			target.setUser(trgt);
		}

		if (taskEntity.getProject() != null) {
			target.setProjectId(taskEntity.getProject().getProjectId());
			target.setProjectName(taskEntity.getProject().getProject());
		}

		return target;
	}

	@Override
	public Set<TaskDTO> getMappedTaskDtoSet(Set<Task> taskEntitySet) {
		Set<TaskDTO> taskDtoSetTarget = new LinkedHashSet<>();

		for (Task source : taskEntitySet) {
			TaskDTO target = new TaskDTO();
			BeanUtils.copyProperties(source, target);

			if (source.getParentTask() != null) {
				ParentTaskDTO trgt = getMappedParentTaskDto(source.getParentTask());
				target.setParentTask(trgt);
			}

			if (source.getUser() != null) {
				UserDTO trgt = getMappedUserDto(source.getUser());
				target.setUser(trgt);
			}

			if (source.getProject() != null) {
				target.setProjectId(source.getProject().getProjectId());
				target.setProjectName(source.getProject().getProject());
			}

			taskDtoSetTarget.add(target);

		}

		return taskDtoSetTarget;
	}

	@Override
	public Set<Task> getMappedTaskEntitySet(Set<TaskDTO> taskDtoSet) {
		Set<Task> taskEntitySetTarget = new LinkedHashSet<>();

		for (TaskDTO source : taskDtoSet) {
			Task target = new Task();
			BeanUtils.copyProperties(source, target);

			if (source.getParentTask() != null) {
				ParentTask trgt = getMappedParentTaskEntity(source.getParentTask());
				target.setParentTask(trgt);
			}

			if (source.getUser() != null) {
				User trgt = getMappedUserEntity(source.getUser());
				target.setUser(trgt);
			}

			if (source.getProjectName() != null) {
				ProjectDTO projDto = new ProjectDTO(source.getProjectId(), source.getProjectName(), null, null, 0);
				Project projTrgt = getMappedProjectEntity(projDto);
				target.setProject(projTrgt);
			}

			taskEntitySetTarget.add(target);
		}

		return taskEntitySetTarget;
	}

	@Override
	public ParentTask getMappedParentTaskEntity(ParentTaskDTO parentTaskDto) {
		ParentTask target = new ParentTask();
		BeanUtils.copyProperties(parentTaskDto, target);

		return target;
	}

	@Override
	public ParentTaskDTO getMappedParentTaskDto(ParentTask parentTaskEntity) {
		ParentTaskDTO target = new ParentTaskDTO();
		BeanUtils.copyProperties(parentTaskEntity, target);

		return target;
	}

	@Override
	public Set<ParentTaskDTO> getMappedParentTaskDtoSet(Set<ParentTask> parentTaskEntitySet) {
		Set<ParentTaskDTO> parentTaskDtoSetTarget = new LinkedHashSet<>();

		for (ParentTask source : parentTaskEntitySet) {
			ParentTaskDTO target = new ParentTaskDTO();
			BeanUtils.copyProperties(source, target);
			parentTaskDtoSetTarget.add(target);
		}

		return parentTaskDtoSetTarget;
	}

	@Override
	public User getMappedUserEntity(UserDTO userDto) {
		User target = new User();
		BeanUtils.copyProperties(userDto, target);

		return target;
	}

	@Override
	public UserDTO getMappedUserDto(User userEntity) {
		UserDTO target = new UserDTO();
		BeanUtils.copyProperties(userEntity, target);

		return target;
	}

	@Override
	public Set<UserDTO> getMappedUserDtoSet(Set<User> userEntitySet) {
		Set<UserDTO> userDtoSetTarget = new LinkedHashSet<>();

		for (User source : userEntitySet) {
			UserDTO target = new UserDTO();
			BeanUtils.copyProperties(source, target);

			userDtoSetTarget.add(target);
		}
		return userDtoSetTarget;
	}

	@Override
	public Project getMappedProjectEntity(ProjectDTO projectDto) {
		Project target = new Project();
		BeanUtils.copyProperties(projectDto, target);

		if (projectDto.getUser() != null) {
			User trgt = getMappedUserEntity(projectDto.getUser());
			target.setUser(trgt);
		}

		if (projectDto.getTasks() != null) {
			Set<Task> taskEntitySet = getMappedTaskEntitySet(projectDto.getTasks());
			target.setTasks(taskEntitySet);
		}

		return target;
	}

	@Override
	public ProjectDTO getMappedProjectDto(Project projectEntity) {
		ProjectDTO target = new ProjectDTO();
		BeanUtils.copyProperties(projectEntity, target);

		if (projectEntity.getUser() != null) {
			UserDTO trgt = getMappedUserDto(projectEntity.getUser());
			target.setUser(trgt);
		}

		if (projectEntity.getTasks() != null) {
			Set<TaskDTO> taskDtoSet = getMappedTaskDtoSet(projectEntity.getTasks());
			target.setTasks(taskDtoSet);
		}

		return target;
	}

	@Override
	public Set<ProjectDTO> getMappedProjectDtoSet(Set<Project> projectEntitySet) {
		Set<ProjectDTO> projectDtoSetTarget = new LinkedHashSet<>();

		for (Project source : projectEntitySet) {
			ProjectDTO target = new ProjectDTO();
			BeanUtils.copyProperties(source, target);

			if (source.getUser() != null) {
				UserDTO trgt = getMappedUserDto(source.getUser());
				target.setUser(trgt);
			}
			if (source.getTasks() != null) {
				Set<TaskDTO> trgt = getMappedTaskDtoSet(source.getTasks());
				target.setTasks(trgt);
			}

			projectDtoSetTarget.add(target);
		}
		return projectDtoSetTarget;
	}

}
