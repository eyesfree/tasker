package com.developer.krisi.tasker.model;

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
    public String status;

    @Ignore
    public Task() {
    }

    public Task(@NonNull String name,@NonNull String description,@NonNull String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
