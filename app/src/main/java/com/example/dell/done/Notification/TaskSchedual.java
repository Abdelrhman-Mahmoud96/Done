package com.example.dell.done.Notification;

import android.app.SearchManager;
import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;


public class TaskSchedual {

    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES) );
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;

    private static final String REMINDER_JOB_TAG = "task_reminder_tag";
    private static boolean initialized;

    synchronized public static void taskReminder(Context context)
    {
        if(initialized)
        {
            return;
        }
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(driver);
        Job job = jobDispatcher.newJobBuilder()
                .setService(TaskJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setTrigger(Trigger.executionWindow(SYNC_FLEXTIME_SECONDS,SYNC_FLEXTIME_SECONDS+REMINDER_INTERVAL_SECONDS ))
                .setConstraints(Constraint.DEVICE_IDLE)
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .build();

        jobDispatcher.schedule(job);
        initialized = true;
    }
}
