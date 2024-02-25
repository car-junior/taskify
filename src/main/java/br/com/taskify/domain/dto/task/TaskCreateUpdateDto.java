package br.com.taskify.domain.dto.task;

import br.com.taskify.domain.dto.task.groups.Create;
import br.com.taskify.domain.dto.task.groups.Update;
import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@GroupSequence({BasicInfo.class, AdvanceInfo.class})
public class TaskCreateUpdateDto {
    @NotNull(groups = {Create.class, Update.class})
    @NotEmpty(groups = {Create.class, Update.class})
    private String name;

    private String comments;

    @NotNull(groups = {Create.class, Update.class})
    @NotEmpty(groups = {Create.class, Update.class})
    private String description;

    @NotNull(groups = Update.class)
    private TaskPriority priority;
}
