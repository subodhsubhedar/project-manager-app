package com.myapp.projectmanager.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "project")
public class Project implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3373094650936392401L;

	@Id
	@GeneratedValue
	@Column(name = "Project_ID", nullable = false)
	private long projectId;

	@NotEmpty(message = "{project.proje.invalid}")
	@Column(name = "Project")
	private String project;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Nullable
	@Column(name = "Start_Date")
	private LocalDate startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Nullable
	@Column(name = "End_Date")
	private LocalDate endDate;

	@NotNull(message = "{task.priority.invalid}")
	@Min(message = "{task.priority.negativeOrZero}", value = 1)
	@Column(name = "Priority")
	private int priority;

	@JsonIgnore
	@OneToMany(mappedBy = "project")
	private Set<Task> tasks;

	@JsonIgnore
	@Nullable
	@OneToOne(mappedBy = "project")
	private User user;

	public Project() {

	}

	public Project(long projectId, String project, LocalDate startDate, LocalDate endDate, int priority) {
		super();
		this.projectId = projectId;
		this.project = project;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

}
