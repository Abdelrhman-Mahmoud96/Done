package com.example.dell.done.AAC;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors instance;
    private Executor diskIO;

    public AppExecutors (Executor diskIO)
    {
        this.diskIO = diskIO;
    }

    public static AppExecutors getInstance()
    {
        if (instance == null)
        {
            instance = new AppExecutors(Executors.newSingleThreadExecutor());
        }
        return instance;
    }
 private class MainTreadExcutor implements Executor
 {
     private Handler handler = new Handler(Looper.getMainLooper());

     @Override
     public void execute(@NonNull Runnable command) {
        handler.post(command);
     }
 }

    public Executor getDiskIO() {
        return diskIO;
    }
}
