package br.com.taskify.domain.spec.search;

import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearch {
    private String query;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate startDate;
    private LocalDate endDate;
}
