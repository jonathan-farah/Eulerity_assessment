package com.example.taskmanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanager.ai.AiTaskSuggestionClient;
import com.example.taskmanager.dto.AiTaskSuggestionResponse;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.TaskStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration-style controller test for /tasks/suggest with AI client mocked.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AiTaskSuggestionEndpointTest {

    // MockMvc executes HTTP requests against the Spring MVC stack.
    @Autowired
    private MockMvc mockMvc;

    // External AI call is mocked to keep test deterministic and offline.
    @MockBean
    private AiTaskSuggestionClient aiTaskSuggestionClient;

    @Test
    void suggestEndpoint_returnsStructuredResponseUsingMockedClient() throws Exception {
        // Arrange mocked AI output.
        AiTaskSuggestionResponse response = new AiTaskSuggestionResponse();
        response.setTitle("Submit quarterly report");
        response.setDescription("Compile metrics and submit report");
        response.setDueDate(LocalDate.of(2026, 4, 3));
        response.setPriority(Priority.HIGH);
        response.setStatus(TaskStatus.TODO);
        response.setModelUsed("mocked-model");

        when(aiTaskSuggestionClient.suggestTask("remind me to submit the quarterly report before Friday"))
            .thenReturn(response);

                // Build request payload.
        String requestBody = """
            {
              "prompt": "remind me to submit the quarterly report before Friday"
            }
            """;

                // Execute endpoint and assert structured JSON response.
        mockMvc.perform(post("/tasks/suggest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Submit quarterly report"))
            .andExpect(jsonPath("$.priority").value("HIGH"))
            .andExpect(jsonPath("$.status").value("TODO"))
            .andExpect(jsonPath("$.modelUsed").value("mocked-model"));
    }
}
