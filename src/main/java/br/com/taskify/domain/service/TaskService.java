package br.com.taskify.domain.service;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import br.com.taskify.domain.infrastructure.CustomException;
import br.com.taskify.domain.repository.TaskRepository;
import br.com.taskify.domain.spec.TaskSpecification;
import br.com.taskify.domain.spec.search.TaskSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static br.com.taskify.domain.entity.enums.TaskStatus.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final Map<TaskStatus, Set<TaskStatus>> statusCanChangeTo = Map.of(
            NOT_STARTED, Set.of(IN_PROGRESS, FINISHED),
            IN_PROGRESS, Set.of(FINISHED),
            FINISHED, Collections.emptySet()
    );

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

    public List<Task> getAllTask(TaskSearch taskSearch) {
        return taskRepository.findAll(TaskSpecification.getAll(taskSearch));
    }

    public void deleteTaskById(Long taskId) {
        assertExistsTask(taskId);
        taskRepository.deleteById(taskId);
    }

    public void changeStatusById(TaskStatus newStatus, Long taskId) {
        var task = getTaskById(taskId);
        assertChangeStatus(task, newStatus);
        taskRepository.updateStatusById(newStatus, task.getId());
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

    private void assertExistsTask(long taskId) {
        if (!taskRepository.existsById(taskId))
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(String.format("Cannot found task with ID %d.", taskId))
                    .build();
    }

    private void assertChangeStatus(Task task, TaskStatus newStatus) {
        var statuses = statusCanChangeTo.entrySet()
                .stream()
                .filter(it -> it.getKey().equals(task.getStatus()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> CustomException.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message(String.format("Cannot found status %s to change.", task.getStatus()))
                        .build());

        var canChangeTo = statuses.contains(newStatus);

        if (!canChangeTo)
            throw CustomException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(String.format("Cannot change status %s to %s.", task.getStatus(), newStatus))
                    .build();
    }
}
