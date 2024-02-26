package br.com.taskify.domain.service;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.infrastructure.CustomException;
import br.com.taskify.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static br.com.taskify.domain.entity.enums.TaskStatus.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        validation(task);
        return taskRepository.save(task.toBuilder()
                .priority(Optional.ofNullable(task.getPriority()).orElse(TaskPriority.LOW))
                .createdDate(LocalDate.now())
                .build()
        );
    }

    public Task updateTask(long taskId, Task updatedTask) {
        var task = getTaskById(taskId);
        updatedTask.setId(taskId);
        validation(updatedTask);
        return taskRepository.save(task.toBuilder()
                .name(updatedTask.getName())
                .priority(updatedTask.getPriority())
                .comments(updatedTask.getComments())
                .description(updatedTask.getDescription())
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

    public List<Task> getAllTask() {
        return taskRepository.findAll();
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
                            "Already exists task %s %s or %s.",
                            task.getName(),
                            NOT_STARTED.getValue(),
                            IN_PROGRESS.getValue())
                    )
                    .build();
    }

}
