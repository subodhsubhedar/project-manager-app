package com.myapp.projectmanager.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "task")
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3373094650936392401L;

	@Id
	@GeneratedValue
	@Column(name = "Task_ID", nullable = false)
	private long taskId;

	@NotEmpty(message = "{task.task.invalid}")
	@Column(name = "Task")
	private String task;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "{task.startDate.invalid}")
	@Column(name = "Start_Date")
	private LocalDate startDate;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "{task.endDate.invalid}")
	@Column(name = "End_Date")
	private LocalDate endDate;

	@NotNull(message = "{task.priority.invalid}")
	@Min(message = "{task.priority.negativeOrZero}", value = 1)
	@Column(name = "Priority")
	private int priority;

	@Nullable
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private ParentTask prntTask;

	@Nullable
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private Project project;
	
	@Nullable
	@Column(name = "Is_Complete")
	private Boolean taskComplete;


	public Task() {

	}

	
	public Task(long taskId, @NotEmpty(message = "{task.task.invalid}") String task,
			@NotNull(message = "{task.startDate.invalid}") LocalDate startDate,
			@NotNull(message = "{task.endDate.invalid}") LocalDate endDate,
			@NotNull(message = "{task.priority.invalid}") @Min(message = "{task.priority.negativeOrZero}", value = 1) int priority,
			ParentTask prntTask, Project project, Boolean taskComplete) {
		super();
		this.taskId = taskId;
		this.task = task;
		this.startDate = startDate;
		this.endDate = endDate;
		this.priority = priority;
		this.prntTask = prntTask;
		this.project = project;
		this.taskComplete = taskComplete;
	}


	public ParentTask getPrntTask() {
		return prntTask;
	}

	public void setPrntTask(ParentTask prntTask) {
		this.prntTask = prntTask;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
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

	public ParentTask getParentTask() {
		return prntTask;
	}

	public void setParentTask(ParentTask parentTask) {
		this.prntTask = parentTask;
	}

	public Boolean getTaskComplete() {
		return taskComplete;
	}

	public void setTaskComplete(Boolean taskComplete) {
		this.taskComplete = taskComplete;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", task=" + task + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", priority=" + priority + ", taskComplete=" + taskComplete + "]";
	}

}
