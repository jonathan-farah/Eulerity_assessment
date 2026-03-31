package com.example.taskmanager.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;
import com.example.taskmanager.repository.TaskRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for TaskService business logic with repository mocked.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // Mocked persistence dependency.
    @Mock
    private TaskRepository taskRepository;

    // Service under test with mocks injected.
    @InjectMocks
    private TaskService taskService;

    // Reusable valid request object for happy-path tests.
    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        // Prepare a representative request payload.
        taskRequest = new TaskRequest();
        taskRequest.setTitle("Ship intern assessment");
        taskRequest.setDescription("Finish backend task manager");
        taskRequest.setDueDate(LocalDate.of(2026, 3, 30));
        taskRequest.setPriority(Priority.HIGH);
        taskRequest.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Test
    void createTask_returnsSavedTask() {
        // Arrange repository save behavior.
        Task saved = new Task();
        saved.setId(1L);
        saved.setTitle(taskRequest.getTitle());
        saved.setDescription(taskRequest.getDescription());
        saved.setDueDate(taskRequest.getDueDate());
        saved.setPriority(taskRequest.getPriority());
        saved.setStatus(taskRequest.getStatus());

        when(taskRepository.save(org.mockito.ArgumentMatchers.any(Task.class))).thenReturn(saved);

        // Act: create task.
        Task result = taskService.createTask(taskRequest);

        // Assert created task values.
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Ship intern assessment");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void getAllTasks_returnsAllTasks() {
        // Arrange two tasks in the mocked repository response.
        Task first = new Task();
        first.setId(1L);
        first.setTitle("A");
        first.setPriority(Priority.LOW);
        first.setStatus(TaskStatus.TODO);

        Task second = new Task();
        second.setId(2L);
        second.setTitle("B");
        second.setPriority(Priority.MEDIUM);
        second.setStatus(TaskStatus.DONE);

        when(taskRepository.findAll()).thenReturn(List.of(first, second));

        // Act: fetch all tasks.
        List<Task> result = taskService.getAllTasks();

        // Assert list size.
        assertThat(result).hasSize(2);
    }

    @Test
    void getTaskById_returnsTaskWhenFound() {
        // Arrange one task for id lookup.
        Task task = new Task();
        task.setId(7L);
        task.setTitle("Read docs");
        task.setPriority(Priority.MEDIUM);
        task.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(7L)).thenReturn(Optional.of(task));

        // Act + assert value mapping.
        Task result = taskService.getTaskById(7L);

        assertThat(result.getId()).isEqualTo(7L);
    }

    @Test
    void updateTask_updatesAndReturnsTask() {
        // Arrange existing entity and save behavior.
        Task existing = new Task();
        existing.setId(9L);
        existing.setTitle("Old");
        existing.setPriority(Priority.LOW);
        existing.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(9L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        // Act: apply update request.
        Task updated = taskService.updateTask(9L, taskRequest);

        // Assert that mutable fields changed.
        assertThat(updated.getTitle()).isEqualTo("Ship intern assessment");
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void deleteTask_deletesExistingTask() {
        // Arrange an existing task for delete lookup.
        Task existing = new Task();
        existing.setId(5L);
        existing.setTitle("Cleanup");
        existing.setPriority(Priority.MEDIUM);
        existing.setStatus(TaskStatus.TODO);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(existing));

        // Act: delete by id.
        taskService.deleteTask(5L);

        // Assert repository delete was invoked.
        verify(taskRepository).delete(existing);
    }

    @Test
    void getTaskById_throwsWhenMissing() {
        // Arrange missing id.
        when(taskRepository.findById(111L)).thenReturn(Optional.empty());

        // Assert translated domain exception.
        assertThatThrownBy(() -> taskService.getTaskById(111L))
            .isInstanceOf(TaskNotFoundException.class)
            .hasMessageContaining("111");
    }
}
