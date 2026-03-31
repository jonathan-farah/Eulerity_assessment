package com.example.taskmanager.exception;

/**
 * Raised when an operation targets a task id that does not exist.
 */
public class TaskNotFoundException extends RuntimeException {

	/**
	 * Builds an exception message for a missing task id.
	 */
	public TaskNotFoundException(Long id) {
		super("Task not found with id: " + id);
	}
}
