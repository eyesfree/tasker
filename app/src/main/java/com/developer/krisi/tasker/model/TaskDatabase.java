package com.developer.krisi.tasker.model;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 2)
public abstract class TaskDatabase extends RoomDatabase {
    public static final String TASKS = "tasks";

    public static final Integer NUMBER_OF_THREADS = 4;

    public abstract TaskDao taskDao();

    private static volatile TaskDatabase INSTANCE;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static TaskDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, TASKS).addCallback(sRoomDatabaseCallback).build();
                }
            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    databaseWriteExecutor.execute(()->{
                        TaskDao taskDao = INSTANCE.taskDao();
                    });
                }
            };
}
