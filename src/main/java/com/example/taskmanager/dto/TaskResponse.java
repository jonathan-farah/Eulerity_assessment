package com.example.taskmanager.dto;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import java.time.LocalDate;

/**
 * Outgoing payload returned by CRUD task endpoints.
 */
public record TaskResponse(
	Long id,
	String title,
	String description,
	LocalDate dueDate,
	Priority priority,
	TaskStatus status
) {
	/**
	 * Converts a Task entity into API response shape.
	 */
	public static TaskResponse fromEntity(Task task) {
		return new TaskResponse(
			task.getId(),
			task.getTitle(),
			task.getDescription(),
			task.getDueDate(),
			task.getPriority(),
			task.getStatus()
		);
	}
}
