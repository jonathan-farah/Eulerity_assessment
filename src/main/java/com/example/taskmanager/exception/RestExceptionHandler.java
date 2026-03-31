package com.example.taskmanager.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized translation from Java exceptions to API error responses.
 */
@RestControllerAdvice
public class RestExceptionHandler {

	/** Maps missing task errors to HTTP 404. */
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleTaskNotFound(TaskNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(Map.of("error", ex.getMessage()));
	}

	/** Maps bean validation failures to HTTP 400 with field-level details. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		// Flatten field errors into a simple {field: message} payload.
		ex.getBindingResult().getFieldErrors()
			.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}

	/** Maps AI/configuration runtime issues to HTTP 503. */
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
			.body(Map.of("error", ex.getMessage()));
	}
}
