package com.example.dell.done.Notification;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.dell.done.AAC.MainViewModel;
import com.example.dell.done.MainActivity;
import com.example.dell.done.Room.TaskEntry;
import com.example.dell.done.Service.DeleteTask;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskJobService extends JobService {

    AsyncTask asyncTask = null;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(@NonNull final JobParameters job) {
        asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                jobFinished(job, false);
            }
        };

        asyncTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
       if (asyncTask != null)
       {
           asyncTask.cancel(true);
       }
        return true;
    }



    private List<Integer> convertDateToNumbers(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        String dateString = simpleDateFormat.format(date);
        String[] dateList = dateString.split("/");
        List<Integer> dateNumbers = new ArrayList<>();

        for(int i = 0;i < dateList.length;i++)
        {
            dateNumbers.add(Integer.parseInt(dateList[i]));
        }
        return dateNumbers;
    }

    private List<Integer> convertTimeToList(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        String dateString = simpleDateFormat.format(date);
        String[] dateList = dateString.split(":");
        List<Integer> dateNumbers = new ArrayList<>();

        for(int i = 0;i < dateList.length;i++)
        {
            dateNumbers.add(Integer.parseInt(dateList[i]));
        }

        return dateNumbers;
    }
}
