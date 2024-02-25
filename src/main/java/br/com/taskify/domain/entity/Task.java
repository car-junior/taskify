package br.com.taskify.domain.entity;


import br.com.taskify.domain.entity.enums.TaskPriority;
import br.com.taskify.domain.entity.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "task")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status = TaskStatus.NOT_STARTED;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "finished_date")
    private LocalDate finishedDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority = TaskPriority.LOW;

    @Column(name = "comments")
    private String comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
