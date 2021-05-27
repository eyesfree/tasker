package com.developer.krisi.tasker.data.model;

import android.content.Context;

import com.developer.krisi.tasker.model.TaskDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

public abstract class ProjectDatabase extends RoomDatabase {

    public static final String PROJECT = "tasksProject";

    public static final Integer NUMBER_OF_THREADS = 4;

    public abstract ProjectDao projectDao();

    private static volatile ProjectDatabase INSTANCE;

    public static final ExecutorService taskDatabaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ProjectDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TaskDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProjectDatabase.class, PROJECT).addCallback(sRoomDatabaseCallback).build();

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
                    taskDatabaseWriteExecutor.execute(()->{
                        ProjectDao projectDao = INSTANCE.projectDao();
                    });
                }
            };

}
