package com.developer.krisi.tasker.data.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM TasksProject WHERE id = (:id)")
    TasksProject getById(String id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(TasksProject project);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void update(TasksProject project);
}
