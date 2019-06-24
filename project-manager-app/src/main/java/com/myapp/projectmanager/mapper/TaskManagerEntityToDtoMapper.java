package com.myapp.projectmanager.mapper;

import java.util.Set;

import com.myapp.projectmanager.dto.ParentTaskDTO;
import com.myapp.projectmanager.dto.TaskDTO;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Task;

/**
 * 
 * @author Admin
 *
 */
public interface TaskManagerEntityToDtoMapper {

	Task getMappedTaskEntity(TaskDTO taskDto);

	TaskDTO getMappedTaskDto(Task taskEntity);

	Set<TaskDTO> getMappedTaskDtoSet(Set<Task> taskEntitySet);

	Set<ParentTaskDTO> getMappedParentTaskDtoSet(Set<ParentTask> parentTaskEntitySet);

	ParentTask getMappedParentTaskEntity(ParentTaskDTO parentTaskDto);

	ParentTaskDTO getMappedParentTaskDto(ParentTask parentTaskEntity);
}
