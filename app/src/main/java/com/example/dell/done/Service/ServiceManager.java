package com.example.dell.done.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.dell.done.Room.TaskEntry;

public class ServiceManager extends IntentService {

    public ServiceManager() {
        super("ServiceManager");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String title = intent.getStringExtra("title");
        String action = intent.getStringExtra("action");
        DeleteTask.actionTask(getApplicationContext(),title,action);
    }
}
