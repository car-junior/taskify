package br.com.taskify.domain.entity.enums;

import java.util.Collections;
import java.util.Set;

public enum TaskStatus {
    FINISHED("Finished", Collections.emptySet()),
    IN_PROGRESS("In progress", Set.of(FINISHED)),
    NOT_STARTED("Not started", Set.of(IN_PROGRESS, FINISHED));
    private final String value;
    private final Set<TaskStatus> canChangeTo;
    TaskStatus(String value, Set<TaskStatus> canChangeTo) {
        this.value = value;
        this.canChangeTo = canChangeTo;
    }
    public String getValue() {
        return value;
    }

    public Set<TaskStatus> getCanChangeTo() {
        return canChangeTo;
    }
}
