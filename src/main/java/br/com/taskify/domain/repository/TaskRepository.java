package br.com.taskify.domain.repository;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    boolean existsByNameAndIdNotAndStatusNot(String name, long id, TaskStatus status);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Task SET status = :status WHERE id = :id")
    void updateStatusById(@Param("status") TaskStatus status, @Param("id") long id);
}
