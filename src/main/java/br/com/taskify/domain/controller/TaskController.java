package br.com.taskify.domain.controller;

import br.com.taskify.domain.dto.task.TaskCreateDto;
import br.com.taskify.domain.dto.task.TaskDetailDto;
import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.service.ModelMapperService;
import br.com.taskify.domain.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ModelMapperService modelMapperService;
    @PostMapping
    public ResponseEntity<TaskDetailDto> createTask(@RequestBody @Valid TaskCreateDto taskCreateDto) {
        var task = taskService.createUpdateTask(modelMapperService.toObject(Task.class, taskCreateDto));
        return ResponseEntity.ok(modelMapperService.toObject(TaskDetailDto.class, task));
    }
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailDto> getTaskById(@PathVariable(name = "taskId") Long taskId) {
        var task  = taskService.getTaskById(taskId);
        return ResponseEntity.ok(modelMapperService.toObject(TaskDetailDto.class, task));
    }
}
