package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service layer for CRUD operations on {@link Task} entities.
 */
@Service
public class TaskService {

	// Repository abstraction used to read/write task records.
	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	/**
	 * Creates and persists a task from request payload values.
	 */
	public Task createTask(TaskRequest request) {
		Task task = new Task();
		applyRequest(task, request);
		return taskRepository.save(task);
	}

	/**
	 * Returns all tasks currently stored in the database.
	 */
	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	/**
	 * Fetches one task by id or throws when it does not exist.
	 */
	public Task getTaskById(Long id) {
		return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
	}

	/**
	 * Updates an existing task in place and persists the changes.
	 */
	public Task updateTask(Long id, TaskRequest request) {
		Task existing = getTaskById(id);
		applyRequest(existing, request);
		return taskRepository.save(existing);
	}

	/**
	 * Deletes an existing task by id.
	 */
	public void deleteTask(Long id) {
		Task existing = getTaskById(id);
		taskRepository.delete(existing);
	}

	/**
	 * Copies incoming DTO values into a task entity.
	 */
	private void applyRequest(Task task, TaskRequest request) {
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setDueDate(request.getDueDate());
		task.setPriority(request.getPriority());
		task.setStatus(request.getStatus());
	}
}
