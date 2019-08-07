package com.myapp.projectmanager.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProjectDTO {

	private long projectId;

	@NotEmpty(message = "{project.project.invalid}")
	private String project;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Nullable
	@FutureOrPresent(message = "{project.startDate.past}")
	private LocalDate startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Nullable
	@FutureOrPresent(message = "{project.endDate.past}")
	private LocalDate endDate;

	@NotNull(message = "{project.priority.invalid}")
	@Min(message = "{project.priority.negativeOrZero}", value = 1)
	private int priority;

	@JsonIgnore
	private Set<TaskDTO> tasks;

	@Nullable
	private UserDTO user;

	public ProjectDTO() {
		this.startDate = LocalDate.now();
		this.endDate = LocalDate.now();
		this.priority = 1;
	}

	public ProjectDTO(long projectId, String project, LocalDate startDate, LocalDate endDate, int priority) {
		super();
		this.projectId = projectId;
		this.project = project;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Set<TaskDTO> getTasks() {
		return tasks;
	}

	public void setTasks(Set<TaskDTO> tasks) {
		this.tasks = tasks;
	}

}
