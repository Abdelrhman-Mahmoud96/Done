package com.example.dell.done;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.MenuItem;

import com.example.dell.done.AAC.AppExecutors;
import com.example.dell.done.AAC.MainViewModel;
import com.example.dell.done.Adapters.RecyclerViewAdapter;
import com.example.dell.done.Adapters.RecyclerViewItemClickListner;
import com.example.dell.done.Room.AppDatabase;
import com.example.dell.done.Room.TaskEntry;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewItemClickListner {

    RecyclerViewAdapter adapter = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditTaskActivity.class);
                startActivity(intent);
            }
        });

        final RecyclerView taskList = findViewById(R.id.tasksList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        taskList.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(this,this);
        taskList.setAdapter(adapter);

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getLiveData().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntry> taskEntries) {
                adapter.setTasks(taskEntries);

            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                deleteTask(position);

            }
        }).attachToRecyclerView(taskList);



    }

    public void deleteTask(final int pos) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<TaskEntry> taskEntries = adapter.getTaskEntries();
//                TaskEntry taskEntry = AppDatabase.getInstance(getApplicationContext()).taskDao().loadTaskById2(pos+1);
                AppDatabase.getInstance(getApplicationContext()).taskDao().deleteTask(taskEntries.get(pos));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(int position) {
        Intent editIntent = new Intent(MainActivity.this,EditTaskActivity.class);
        List<TaskEntry> taskEntry = adapter.getTaskEntries();
        editIntent.putExtra("taskId",taskEntry.get(position).getId());
        startActivity(editIntent);


    }
}
