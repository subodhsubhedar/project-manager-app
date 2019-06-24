package com.myapp.projectmanager.exception;

/**
 * 
 * @author Admin
 *
 */

public class ProjectManagerServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectManagerServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ProjectManagerServiceException(String message) {
		super(message);
	}

}
