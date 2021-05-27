package com.developer.krisi.tasker.model;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {
    /**
     * Returns an automatically refreshed list with
     * up-to date Wearables ordered alphabetically.
     * @return List<Task>
     */
    @Query("SELECT * FROM tasks WHERE projectId = (:projectId) ORDER BY name ASC")
    LiveData<List<Task>> getAll(String projectId);

    @Query("SELECT * FROM tasks WHERE id IN (:ids)")
    List<Task> loadAllByIds(int[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Insert
    void insertAll(Task... tasks);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks WHERE id = (:id)")
    void deleteById(int id);

    @Query("DELETE from tasks")
    void deleteAll();

    @Update
    void update(Task task);

    @Query("SELECT * FROM tasks WHERE id = (:id)")
    Task getById(String id);
}
