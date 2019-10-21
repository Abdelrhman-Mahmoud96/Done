package com.example.dell.done.AAC;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;


import com.example.dell.done.Room.AppDatabase;

public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private AppDatabase mDatabase;
    private int mId;

    public AppViewModelFactory(AppDatabase database, int id)
    {
        mDatabase = database;
        mId = id;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AppViewModel(mDatabase,mId);
    }
}
