package com.example.taskmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * End-to-end CRUD API test using real Spring context and in-memory database.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    // Performs HTTP requests without starting an external server.
    @Autowired
    private MockMvc mockMvc;

    // Used for verifying persisted state between API calls.
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void clearDatabase() {
        // Ensure isolation between test runs.
        taskRepository.deleteAll();
    }

    @Test
    void crudEndpoints_workEndToEnd() throws Exception {
        // Create payload for POST /tasks.
        String createBody = """
            {
              "title": "Prepare sprint demo",
              "description": "Collect updates",
              "dueDate": "2026-04-01",
              "priority": "HIGH",
              "status": "TODO"
            }
            """;

        // Update payload for PUT /tasks/{id}.
        String updateBody = """
            {
              "title": "Prepare sprint demo v2",
              "description": "Collect updates and rehearse",
              "dueDate": "2026-04-02",
              "priority": "MEDIUM",
              "status": "IN_PROGRESS"
            }
            """;

        // Create task.
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.title").value("Prepare sprint demo"));

        // Capture created id for follow-up requests.
        Task created = taskRepository.findAll().stream().findFirst().orElseThrow();

        // List tasks.
        mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(created.getId()));

        // Get task by id.
        mockMvc.perform(get("/tasks/{id}", created.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Prepare sprint demo"));

        // Update task.
        mockMvc.perform(put("/tasks/{id}", created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Prepare sprint demo v2"))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        // Delete task.
        mockMvc.perform(delete("/tasks/{id}", created.getId()))
            .andExpect(status().isNoContent());

        // Verify resource is gone.
        mockMvc.perform(get("/tasks/{id}", created.getId()))
            .andExpect(status().isNotFound());
    }
}
