package br.com.taskify.domain.service;

import br.com.taskify.domain.dto.task.TaskDetailDto;
import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import br.com.taskify.domain.infrastructure.CustomException;
import br.com.taskify.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static br.com.taskify.domain.entity.enums.TaskStatus.IN_PROGRESS;
import static br.com.taskify.domain.entity.enums.TaskStatus.NOT_STARTED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
        var savedTask = taskService.createTask(taskOne);

        // Then / Assert
        assertNotNull(savedTask);
        assertNotNull(savedTask.getCreatedDate());
        assertEquals(1L, savedTask.getId());
        assertEquals(TaskStatus.NOT_STARTED, savedTask.getStatus());
        assertEquals(TaskPriority.LOW, savedTask.getPriority());
    }

    @Test
    void testGivenExistingTaskName_whenCreateTask_thenThrowsCustomException() {
        // Given / Arrange
        given(taskRepository.existsByNameAndIdNotAndStatusNot(anyString(), anyLong(), any(TaskStatus.class)))
                .willReturn(true);

        // When / Act
        var customException = assertThrows(CustomException.class, () -> taskService.createTask(taskOne));

        // Then / Assert
        assertEquals(
                String.format(
                        "Already exists task %s %s or %s.",
                        taskOne.getName(),
                        NOT_STARTED.getValue(),
                        IN_PROGRESS.getValue()
                ),
                customException.getMessage()
        );
        verify(taskRepository, never()).save(taskOne);
    }

    @Test
    void testGivenTaskId_whenGetTaskById_thenReturnTaskDetailDto() {
//        // Given / Arrange
//        given(taskRepository.findById(anyLong()))
//                .willReturn(Task.builder().build());
    }
}
