package br.com.taskify.domain.spec;

import br.com.taskify.domain.entity.Task;
import br.com.taskify.domain.entity.Task_;
import br.com.taskify.domain.spec.search.TaskSearch;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

import static br.com.taskify.domain.utility.Utils.*;

public class TaskSpecification {
    private TaskSpecification() {
    }

    public static Specification<Task> getAll(TaskSearch taskSearch) {
        return (root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();

            if (isNotEmpty(taskSearch.getQuery())) {
                predicates.add(builder.or(
                        builder.like(
                                builder.lower(builder.function(UNACCENT, String.class, root.get(Task_.NAME))),
                                formatToLike(taskSearch.getQuery())
                        ),
                        builder.like(
                                builder.lower(builder.function(UNACCENT, String.class, root.get(Task_.DESCRIPTION))),
                                formatToLike(taskSearch.getQuery())
                        ),
                        builder.like(
                                builder.lower(builder.function(UNACCENT, String.class, root.get(Task_.COMMENTS))),
                                formatToLike(taskSearch.getQuery())
                        )
                ));
            }

            if (isPresent(taskSearch.getStatus()))
                predicates.add(builder.equal(root.get(Task_.STATUS), taskSearch.getStatus()));

            if (isPresent(taskSearch.getPriority()))
                predicates.add(builder.equal(root.get(Task_.PRIORITY), taskSearch.getPriority()));

            if (isPresent(taskSearch.getStartDate()))
                predicates.add(
                        builder.and(builder.greaterThanOrEqualTo(root.get(Task_.CREATED_DATE), taskSearch.getStartDate()))
                );

            if (isPresent(taskSearch.getEndDate()))
                predicates.add(
                        builder.and(builder.lessThanOrEqualTo(root.get(Task_.CREATED_DATE), taskSearch.getStartDate()))
                );

            return predicates.isEmpty() ? null : builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
