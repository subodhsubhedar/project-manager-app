package com.myapp.projectmanager.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "parent_task")
public class ParentTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6839843018667052320L;

	@Id
	@Column(name = "Parent_ID", nullable = false)
	private long parentId;

	@NotEmpty(message = "{parentTask.parentTask.invalid}")
	@Column(name = "Parent_Task")
	private String parentTaskDesc;

	@JsonIgnore
	@OneToMany(mappedBy = "prntTask", fetch = FetchType.EAGER)
	private Set<Task> subTasks;

	public ParentTask() {
		// Default constructor
	}

	public ParentTask(long parentId, @NotEmpty(message = "{parentTask.parentTask.invalid}") String parentTaskDesc,
			Set<Task> subTasks) {
		super();
		this.parentId = parentId;
		this.parentTaskDesc = parentTaskDesc;
		this.subTasks = subTasks;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getParentTaskDesc() {
		return parentTaskDesc;
	}

	public void setParentTaskDesc(String parentTaskDesc) {
		this.parentTaskDesc = parentTaskDesc;
	}

	public Set<Task> getSubTasks() {
		return subTasks;
	}

	public void setSubTasks(Set<Task> subTasks) {
		this.subTasks = subTasks;
	}

	public void addSubTasks(Task subTask) {
		if (this.subTasks == null) {
			this.subTasks = new HashSet<Task>();
		}
		this.subTasks.add(subTask);
	}

	@Override
	public String toString() {
		return "ParentTask [parentId=" + parentId + ", parentTaskDesc=" + parentTaskDesc + "]";
	}

}
