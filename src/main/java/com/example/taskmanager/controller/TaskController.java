package com.example.taskmanager.controller;

import com.example.taskmanager.dto.AiTaskSuggestionRequest;
import com.example.taskmanager.dto.AiTaskSuggestionResponse;
import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.AiTaskSuggestionService;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing CRUD endpoints and an AI task suggestion endpoint.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

	// Core business services used by this API surface.
	private final TaskService taskService;
	private final AiTaskSuggestionService aiTaskSuggestionService;

	public TaskController(TaskService taskService, AiTaskSuggestionService aiTaskSuggestionService) {
		this.taskService = taskService;
		this.aiTaskSuggestionService = aiTaskSuggestionService;
	}

	/** Creates a new task from validated request data. */
	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
		Task created = taskService.createTask(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.fromEntity(created));
	}

	/** Lists all persisted tasks. */
	@GetMapping
	public List<TaskResponse> getAllTasks() {
		return taskService.getAllTasks().stream().map(TaskResponse::fromEntity).toList();
	}

	/** Returns one task by id. */
	@GetMapping("/{id}")
	public TaskResponse getTaskById(@PathVariable Long id) {
		return TaskResponse.fromEntity(taskService.getTaskById(id));
	}

	/** Updates an existing task by id. */
	@PutMapping("/{id}")
	public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
		return TaskResponse.fromEntity(taskService.updateTask(id, request));
	}

	/** Deletes a task by id. */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}

	/** Generates a structured task suggestion from natural language input. */
	@PostMapping("/suggest")
	public AiTaskSuggestionResponse suggestTask(@Valid @RequestBody AiTaskSuggestionRequest request) {
		return aiTaskSuggestionService.suggestTask(request.getPrompt());
	}
}
