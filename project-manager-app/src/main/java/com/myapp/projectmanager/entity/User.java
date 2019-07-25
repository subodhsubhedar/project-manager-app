package com.myapp.projectmanager.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7707678124470982446L;

	@Id
	@GeneratedValue
	@Column(name = "User_ID", nullable = false)
	private long userId;

	@GeneratedValue
	@Column(name = "Employee_ID", nullable = false)
	private long empId;

	@NotEmpty(message = "{user.firstName.invalid}")
	@Column(name = "First_Name")
	private String firstName;

	@NotEmpty(message = "{user.lastName.invalid}")
	@Column(name = "Last_Name")
	private String lastName;

	@JsonIgnore
	@Nullable
	@OneToOne
	@JoinColumn(name = "Project_ID")
	private Project project;

	@JsonIgnore
	@Nullable
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Task_ID")
	private Task task;

	public User() {

	}

	public User(long userId, long empId, @NotEmpty(message = "{user.firstName.invalid}") String firstName,
			@NotEmpty(message = "{user.lastName.invalid}") String lastName, Project project, Task task) {
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
