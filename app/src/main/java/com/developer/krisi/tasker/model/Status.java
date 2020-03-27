package com.developer.krisi.tasker.model;

public enum Status {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE"),
    ON_HOLD("ON_HOLD");

    private String statusName;

    Status(final String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return this.statusName;
    }
}
