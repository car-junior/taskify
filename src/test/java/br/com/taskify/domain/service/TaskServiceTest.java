package br.com.taskify.domain.service;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import br.com.taskify.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // Add suporte do mockito para class
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    private Task taskOne;

    @BeforeEach
    public void setup() {
        taskOne = Task.builder()
                .name("Passear com cachorro")
                .description("Levar os cachorros para passear na praça.")
                .createdDate(LocalDate.now())
                .build();
    }


    @Test
    void testGivenTask_whenCreateTask_thenReturnSavedTask() {
        // Given / Arrange
        given(taskRepository.existsByNameAndIdNotAndStatusNot(anyString(), anyLong(), any(TaskStatus.class)))
                .willReturn(false);

        given(taskRepository.save(any(Task.class)))
                .willAnswer(invocation ->
                        ((Task) invocation.getArgument(0))
                                .toBuilder()
                                .id(1L)
                                .build()
                );

        // When / Act
        var savedTask = taskService.createUpdateTask(taskOne);

        // Then / Assert
        assertNotNull(savedTask);
        assertNotNull(savedTask.getCreatedDate());
        assertEquals(1L, savedTask.getId());
        assertEquals(TaskStatus.NOT_STARTED, savedTask.getStatus());
        assertEquals(TaskPriority.LOW, savedTask.getPriority());
    }
}
