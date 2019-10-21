package com.example.dell.done.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM tasks ORDER BY date ")
    LiveData<List<TaskEntry>> loadAllTasks();

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateTask (TaskEntry taskEntry);

    @Delete
    void deleteTask (TaskEntry taskEntry);

    @Query("SELECT * FROM tasks WHERE id = :id  ")
    LiveData<TaskEntry> loadTaskById(int id);

    @Query("SELECT * FROM tasks WHERE title = :title  ")
    TaskEntry loadTaskByTitle(String title);
}
