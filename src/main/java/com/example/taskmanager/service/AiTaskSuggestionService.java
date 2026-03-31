package com.example.taskmanager.service;

import com.example.taskmanager.ai.AiTaskSuggestionClient;
import com.example.taskmanager.dto.AiTaskSuggestionResponse;
import org.springframework.stereotype.Service;

/**
 * Service wrapper around the AI client used by the controller layer.
 */
@Service
public class AiTaskSuggestionService {

	// Client implementation performs the actual external model call.
	private final AiTaskSuggestionClient aiTaskSuggestionClient;

	public AiTaskSuggestionService(AiTaskSuggestionClient aiTaskSuggestionClient) {
		this.aiTaskSuggestionClient = aiTaskSuggestionClient;
	}

	/**
	 * Requests a task suggestion from natural language input.
	 */
	public AiTaskSuggestionResponse suggestTask(String prompt) {
		return aiTaskSuggestionClient.suggestTask(prompt);
	}
}
