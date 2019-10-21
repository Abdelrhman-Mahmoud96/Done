package com.example.dell.done.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.dell.done.Room.TaskEntry;

import androidx.work.ListenableWorker;
import androidx.work.Worker;

public class AlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("taskTitle");
        int id = intent.getIntExtra("taskId",-1 );
        NotificationUtils.taskReminderNotification(context.getApplicationContext(),id+1,title);

    }
}
