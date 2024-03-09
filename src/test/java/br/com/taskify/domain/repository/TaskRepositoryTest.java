package br.com.taskify.domain.repository;

import br.com.taskify.domain.config.TaskifyRepositoryIntegrationTestConfiguration;
import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest extends TaskifyRepositoryIntegrationTestConfiguration {
    private Task task;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        task = Task.builder()
                .name("Passear com cachorro.")
                .createdDate(LocalDate.now())
                .description("Passear com cachorros na praça.")
                .build();
        taskRepository.save(task);
    }

    @Test
    void testGivenNameAndIdAndStatus_whenExistsByNameAndIdNotAndStatusNot_thenReturnFalse() {
        // Given
        var taskTwo = Task.builder()
                .name("Lavar roupas.")
                .build();

        // When
        var result = taskRepository.existsByNameAndIdNotAndStatusNot(
                taskTwo.getName(),
                taskTwo.getId(),
                TaskStatus.FINISHED
        );

        //Then
        assertFalse(result);
    }

    @Test
    void testGivenNameExisting_whenExistsByNameAndIdNotAndStatusNot_thenReturnTrue() {
        // Given
        var taskTwo = Task.builder()
                .name(task.getName())
                .build();

        // When
        var result = taskRepository.existsByNameAndIdNotAndStatusNot(
                taskTwo.getName(),
                taskTwo.getId(),
                TaskStatus.FINISHED
        );

        //Then
        assertTrue(result);
    }

    @Test
    void testGivenNameExistingWithTaskFinished_whenExistsByNameAndIdNotAndStatusNot_thenReturnFalse() {
        // Given
        taskRepository.save(task.toBuilder()
                .status(TaskStatus.FINISHED)
                .build()
        );
        var taskTwo = Task.builder()
                .name(task.getName())
                .build();

        // When
        var result = taskRepository.existsByNameAndIdNotAndStatusNot(
                taskTwo.getName(),
                taskTwo.getId(),
                TaskStatus.FINISHED
        );

        //Then
        assertFalse(result);
    }

    @Test
    void testGivenTaskStatus_whenUpdateStatusById_thenDoNothing() {
        // Given
        var newStatus = TaskStatus.IN_PROGRESS;

        // When
        taskRepository.updateStatusById(newStatus, task.getId());

        //Then
        task = taskRepository.findById(task.getId()).get();
        assertEquals(newStatus, task.getStatus());
    }
}
