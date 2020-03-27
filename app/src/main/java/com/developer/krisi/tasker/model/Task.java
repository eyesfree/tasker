package com.developer.krisi.tasker.model;

import java.time.LocalDateTime;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey
    @ColumnInfo(name="id")
    @NonNull
    public String id;

    @ColumnInfo(name="name")
    @NonNull
    public String name;

    @ColumnInfo(name="description")
    @NonNull
    public String description;

    @ColumnInfo(name="status")
    @NonNull
    public Status status;

    @ColumnInfo(name="assigneeName")
    private String assigneeName;

    @ColumnInfo(name="priority")
    private int priority;

    @ColumnInfo(name="dueDate")
    private LocalDateTime dueDate;

    @ColumnInfo(name="createdDate")
    private LocalDateTime createdDate;

    @ColumnInfo(name="subTasks")
    private List<String> subTasks;

    @Ignore
    public Task() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Task(@NonNull String name, @NonNull String description, @NonNull Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<String> subTasks) {
        this.subTasks = subTasks;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder fullTaskBuilder = new StringBuilder();
        fullTaskBuilder
                .append("Name: ").append(name)
                .append(", Description: ").append(description)
                .append(" Status: ").append(status);
        return fullTaskBuilder.toString();
    }
}
