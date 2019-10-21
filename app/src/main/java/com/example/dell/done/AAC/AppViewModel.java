package com.example.dell.done.AAC;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.dell.done.Room.AppDatabase;
import com.example.dell.done.Room.TaskEntry;

public class AppViewModel extends ViewModel {

    private LiveData<TaskEntry> taskEntryLiveData;

    public AppViewModel(AppDatabase database, int id)
    {
        taskEntryLiveData = database.taskDao().loadTaskById(id);
    }
    public LiveData<TaskEntry> getTaskEntryLiveData() {
        return taskEntryLiveData;

    }
}
