package br.com.taskify.domain.dto.task;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDto {
    @NotNull
    @NotEmpty
    private String name;
    private String description;
    private String comments;
}
