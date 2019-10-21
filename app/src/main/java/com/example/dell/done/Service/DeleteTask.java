package com.example.dell.done.Service;



import android.arch.persistence.room.Database;
import android.content.Context;

import com.example.dell.done.AAC.AppExecutors;
import com.example.dell.done.Adapters.RecyclerViewAdapter;
import com.example.dell.done.Notification.NotificationUtils;
import com.example.dell.done.Room.AppDatabase;
import com.example.dell.done.Room.TaskEntry;

import java.util.List;

public class DeleteTask {


    public static String ACTION_GRANTED = "he finished it";
    public static String ACTION_DISMISSED = "just wait";
    static RecyclerViewAdapter recyclerViewAdapter;

    public static void actionTask(Context context,String title, String action)
    {

        if(action.equals(ACTION_GRANTED))
        {
            deleteTask(context,title);
            NotificationUtils.notificationClear(context);

        }

        else if(action.equals(ACTION_DISMISSED))
        {
           NotificationUtils.notificationClear(context);
        }

    }

    private static void deleteTask(final Context context, final String title)
    {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
               TaskEntry taskEntry = AppDatabase.getInstance(context.getApplicationContext()).taskDao().loadTaskByTitle(title);
                AppDatabase.getInstance(context.getApplicationContext()).taskDao().deleteTask(taskEntry);
            }
        });
    }
}
