package com.myapp.projectmanager.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author Admin
 *
 */
public class ProjectManagerErrorResponse {

	private LocalDateTime dateTime;

	private HttpStatus httpStatus;

	private List<String> errors;

	private String detailedException;

	public ProjectManagerErrorResponse() {
		// Auto-generated constructor stub
	}

	public String getDetailedException() {
		return detailedException;
	}

	public void setDetailedException(String detailedException) {
		this.detailedException = detailedException;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
