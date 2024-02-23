package br.com.taskify.domain.repository;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    boolean existsByNameAndIdNotAndStatusNot(String name, long id, TaskStatus status);
}
