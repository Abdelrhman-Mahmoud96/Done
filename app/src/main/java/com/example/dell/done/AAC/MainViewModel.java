package com.example.dell.done.AAC;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.dell.done.Room.AppDatabase;
import com.example.dell.done.Room.TaskEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<TaskEntry>> liveData;

    public MainViewModel(@NonNull Application application) {
        super(application);

        liveData = AppDatabase.getInstance(this.getApplication()).taskDao().loadAllTasks();
    }

    public LiveData<List<TaskEntry>> getLiveData() {
        return liveData;
    }
}
