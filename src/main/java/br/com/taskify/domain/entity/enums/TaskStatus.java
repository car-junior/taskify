package br.com.taskify.domain.entity.enums;

public enum TaskStatus {
    NOT_STARTED("Not started"),
    IN_PROGRESS("In progress"),
    FINISHED("Finished");
    private final String value;
    TaskStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
