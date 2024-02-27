package br.com.taskify.domain.entity.enums;

public enum TaskStatus {
    FINISHED("Finished"),
    NOT_STARTED("Not started"),
    IN_PROGRESS("In progress");
    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
