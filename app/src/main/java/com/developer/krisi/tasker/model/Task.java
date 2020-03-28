package com.developer.krisi.tasker.model;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
    private Date dueDate;

    @ColumnInfo(name="createdDate")
    private Date createdDate;

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

    public Task(@NonNull String name, @NonNull String description, @NonNull Status status, int priority) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.priority = priority;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
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
