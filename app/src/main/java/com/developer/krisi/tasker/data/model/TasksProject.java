package com.developer.krisi.tasker.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasksProject")
public class TasksProject {
    @PrimaryKey
    @ColumnInfo(name="id")
    @NonNull
    private String id;

    @ColumnInfo(name="name")
    @NonNull
    private String name;

    public TasksProject(String displayName) {
        this.name = displayName;
    }

    public TasksProject(String projectId, String displayName) {
        this.id = projectId;
        this.name = displayName;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

}
