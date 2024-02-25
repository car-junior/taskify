package br.com.taskify.domain.service;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskStatus;
import br.com.taskify.domain.infrastructure.CustomException;
import br.com.taskify.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static br.com.taskify.domain.entity.enums.TaskStatus.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createUpdateTask(Task task) {
        validation(task);
        return taskRepository.save(task.toBuilder()
                .createdDate(LocalDate.now())
                .build()
        );
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> CustomException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message(String.format("Cannot found task with ID: %d", taskId))
                        .build()
                );
    }

    // Methods Privates

    private void validation(Task task) {
        assertNameNotExists(task);
    }

    private void assertNameNotExists(Task task) {
        if (taskRepository.existsByNameAndIdNotAndStatusNot(task.getName(), task.getId(), FINISHED))
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(String.format(
                            "Already exists task with this name in %s or %s.",
                            NOT_STARTED.getValue(),
                            IN_PROGRESS.getValue())
                    )
                    .build();
    }

}
