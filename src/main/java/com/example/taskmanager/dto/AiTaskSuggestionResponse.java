package com.example.taskmanager.dto;

import java.time.LocalDate;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.TaskStatus;

/**
 * Structured AI output returned by /tasks/suggest.
 */
public class AiTaskSuggestionResponse {

	// Core task fields derived from model output.
	private String title;
	private String description;
	private LocalDate dueDate;
	private Priority priority;
	private TaskStatus status;
	// Echoes the model name for observability/debugging.
	private String modelUsed;

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

	public String getModelUsed() {
		return modelUsed;
	}

	public void setModelUsed(String modelUsed) {
		this.modelUsed = modelUsed;
	}
}
