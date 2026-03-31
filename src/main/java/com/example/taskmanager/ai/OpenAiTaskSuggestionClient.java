package com.example.taskmanager.ai;

import com.example.taskmanager.dto.AiTaskSuggestionResponse;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.TaskStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

/**
 * OpenAI-compatible implementation of AI task suggestion generation.
 */
@Component
public class OpenAiTaskSuggestionClient implements AiTaskSuggestionClient {

	// Keep the prompt explicit so the model returns machine-parseable JSON.
	private static final String SYSTEM_PROMPT = """
		You convert natural language into a JSON task object.
		Return JSON only (no markdown), with these keys:
		title (string), description (string), dueDate (string yyyy-MM-dd or null), priority (LOW|MEDIUM|HIGH), status (TODO|IN_PROGRESS|DONE).
		""";

	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	private final AiProperties aiProperties;

	/**
	 * Builds a client with configured base URL and JSON mapper.
	 */
	public OpenAiTaskSuggestionClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper, AiProperties aiProperties) {
		this.restClient = restClientBuilder.baseUrl(aiProperties.getBaseUrl()).build();
		this.objectMapper = objectMapper;
		this.aiProperties = aiProperties;
	}

	@Override
	public AiTaskSuggestionResponse suggestTask(String userPrompt) {
		// Fail fast with a clear message when AI credentials are not configured.
		if (!StringUtils.hasText(aiProperties.getApiKey())) {
			throw new IllegalStateException("AI endpoint is not configured. Set ai.openai.api-key to enable /tasks/suggest.");
		}

		// Compose a compact chat request optimized for deterministic JSON output.
		Map<String, Object> requestBody = Map.of(
			"model", aiProperties.getModel(),
			"messages", List.of(
				Map.of("role", "system", "content", SYSTEM_PROMPT),
				Map.of("role", "user", "content", userPrompt)
			),
			"temperature", 0.2
		);

		// Execute request against the OpenAI-compatible chat completions endpoint.
		JsonNode response = restClient.post()
			.uri("/chat/completions")
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", "Bearer " + aiProperties.getApiKey())
			.body(requestBody)
			.retrieve()
			.body(JsonNode.class);

		if (response == null || response.path("choices").isEmpty()) {
			throw new IllegalStateException("AI model returned an empty response.");
		}

		// Extract the model text response and map it into our response DTO.
		String content = response.path("choices").get(0).path("message").path("content").asText();
		AiTaskSuggestionResponse parsed = parseSuggestion(content);
		parsed.setModelUsed(aiProperties.getModel());
		return parsed;
	}

	/**
	 * Parses model JSON text and fills fallback values when fields are omitted.
	 */
	private AiTaskSuggestionResponse parseSuggestion(String content) {
		try {
			AiTaskSuggestionResponse parsed = objectMapper.readValue(content, AiTaskSuggestionResponse.class);
			// Apply safe defaults when the model omits optional fields.
			if (!StringUtils.hasText(parsed.getTitle())) {
				parsed.setTitle("Suggested Task");
			}
			if (parsed.getPriority() == null) {
				parsed.setPriority(Priority.MEDIUM);
			}
			if (parsed.getStatus() == null) {
				parsed.setStatus(TaskStatus.TODO);
			}
			return parsed;
		} catch (JsonProcessingException ex) {
			throw new IllegalStateException("Failed to parse AI response as JSON.", ex);
		}
	}
}
