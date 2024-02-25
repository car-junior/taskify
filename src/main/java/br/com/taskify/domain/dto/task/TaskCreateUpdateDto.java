package br.com.taskify.domain.dto.task;

import br.com.taskify.domain.entity.enums.TaskPriority;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateUpdateDto {
    @NotNull
    @NotEmpty
    private String name;
    private String comments;
    private String description;
    private TaskPriority priority;
}
