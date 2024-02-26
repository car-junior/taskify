package br.com.taskify.domain.spec;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.Task_;
import br.com.taskify.domain.spec.search.TaskSearch;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Optional;

public class TaskSpecification {
    private TaskSpecification() {}

    public static Specification<Task> getAll(TaskSearch taskSearch) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (Optional.ofNullable(taskSearch.getStatus()).isPresent())
                predicates.add(builder.equal(root.get(Task_.STATUS), taskSearch.getStatus()));

            if (Optional.ofNullable(taskSearch.getPriority()).isPresent())
                predicates.add(builder.equal(root.get(Task_.PRIORITY), taskSearch.getPriority()));

            return predicates.isEmpty() ? null : builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}