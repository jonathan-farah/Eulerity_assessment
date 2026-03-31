package com.example.taskmanager.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * External AI configuration values loaded through Spring Boot binding.
 */
// Binds ai.openai.* values from application.properties or environment variables.
@ConfigurationProperties(prefix = "ai.openai")
public class AiProperties {

	// Base URL allows swapping providers that implement an OpenAI-compatible API.
	private String baseUrl = "https://api.openai.com/v1";
	// Model name sent with each chat completion request.
	private String model = "gpt-4o-mini";
	// Secret credential used in Authorization header.
	private String apiKey = "";

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
