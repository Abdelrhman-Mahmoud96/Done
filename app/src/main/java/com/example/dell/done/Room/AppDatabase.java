package com.example.dell.done.Room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {TaskEntry.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance ;
    private static String DATABASE_NAME = "todo";
    private static Object lock = new Object();

    public static AppDatabase getInstance(Context context)
    {
        if (instance == null) {
            synchronized (lock) {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();

            }
        }
      return instance;
    }

    public abstract TaskDAO taskDao();
}
