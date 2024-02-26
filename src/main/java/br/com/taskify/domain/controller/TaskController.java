package br.com.taskify.domain.controller;

import br.com.taskify.domain.dto.task.TaskCreateUpdateDto;
import br.com.taskify.domain.dto.task.TaskDetailDto;
import br.com.taskify.domain.dto.task.TaskListDto;
import br.com.taskify.domain.dto.task.groups.Create;
import br.com.taskify.domain.dto.task.groups.Update;
import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import br.com.taskify.domain.service.ModelMapperService;
import br.com.taskify.domain.service.TaskService;
import br.com.taskify.domain.spec.search.TaskSearch;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ModelMapperService modelMapperService;

    @PostMapping
    public ResponseEntity<TaskDetailDto> createTask(
            @Validated(Create.class) @RequestBody TaskCreateUpdateDto taskCreateDto) {
        var task = taskService.createTask(modelMapperService.toObject(Task.class, taskCreateDto));
        return ResponseEntity.ok(modelMapperService.toObject(TaskDetailDto.class, task));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailDto> getTaskById(@PathVariable(name = "taskId") Long taskId) {
        var task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(modelMapperService.toObject(TaskDetailDto.class, task));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDetailDto> updateTask(
            @PathVariable(name = "taskId") Long taskId,
            @Validated({Create.class, Update.class}) @RequestBody TaskCreateUpdateDto taskUpdateDto) {
        var task = taskService.updateTask(taskId, modelMapperService.toObject(Task.class, taskUpdateDto));
        return ResponseEntity.ok(modelMapperService.toObject(TaskDetailDto.class, task));
    }

    @GetMapping
    public ResponseEntity<List<TaskListDto>> getAllTask(
            @RequestParam(name = "status", required = false) TaskStatus status,
            @RequestParam(name = "priority", required = false) TaskPriority priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        var taskSearch = TaskSearch.builder()
                .status(status)
                .priority(priority)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        var tasks = taskService.getAllTask(taskSearch);
        return ResponseEntity.ok(modelMapperService.toList(TaskListDto.class, tasks));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable(name = "taskId") Long taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.noContent().build();
    }

    // Criar endpoint para mudar status da task

}
