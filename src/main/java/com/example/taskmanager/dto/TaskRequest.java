package com.example.taskmanager.dto;

import java.time.LocalDate;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.TaskStatus;

import jakarta.validation.constraints.NotBlank;

/**
 * Incoming payload for task create/update endpoints.
 */
public class TaskRequest {

	// Title is required to keep each task actionable.
	@NotBlank(message = "title is required")
	private String title;
	// Optional detail field shown in UI and API responses.
	private String description;
	// Optional target date for completing the task.
	private LocalDate dueDate;
	// Defaults keep the create payload minimal for callers.
	private Priority priority = Priority.MEDIUM;
	private TaskStatus status = TaskStatus.TODO;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}
}
