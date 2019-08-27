package com.myapp.projectmanager.dto;

import javax.validation.constraints.NotEmpty;

public class UserDTO {

	private long userId;

	private long empId;

	@NotEmpty(message = "{user.firstName.invalid}")
	private String firstName;

	@NotEmpty(message = "{user.lastName.invalid}")
	private String lastName;

	public UserDTO() {

	}

	public UserDTO(long userId, long empId, @NotEmpty(message = "{user.firstName.invalid}") String firstName,
			@NotEmpty(message = "{user.lastName.invalid}") String lastName) {
		super();
		this.userId = userId;
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
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

}
