package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Incoming payload for AI suggestion endpoint.
 */
public class AiTaskSuggestionRequest {

	// Natural-language prompt describing the desired task.
	@NotBlank(message = "prompt is required")
	private String prompt;

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
}
