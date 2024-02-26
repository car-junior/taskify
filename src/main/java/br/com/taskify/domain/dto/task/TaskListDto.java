package br.com.taskify.domain.dto.task;

import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskListDto {
    private long id;
    private String name;
    private TaskStatus status;
    private LocalDate createdDate;
    private LocalDate finishedDate;
    private TaskPriority priority;
}
