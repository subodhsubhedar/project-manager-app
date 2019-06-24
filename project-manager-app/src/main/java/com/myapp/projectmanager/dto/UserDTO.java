package com.myapp.projectmanager.dto;

import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

public class UserDTO {

	private long userId;

	private long empId;

	@NotEmpty(message = "{user.firstName.invalid}")
	private String firstName;

	@NotEmpty(message = "{user.lastName.invalid}")
	private String lastName;

	@Nullable
	private ProjectDTO project;

	@Nullable
	private TaskDTO task;

	public UserDTO() {

	}

	public UserDTO(long userId, long empId, @NotEmpty(message = "{user.firstName.invalid}") String firstName,
			@NotEmpty(message = "{user.lastName.invalid}") String lastName, ProjectDTO project, TaskDTO task) {
		super();
		this.userId = userId;
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.project = project;
		this.task = task;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	public TaskDTO getTask() {
		return task;
	}

	public void setTask(TaskDTO task) {
		this.task = task;
	}

}
