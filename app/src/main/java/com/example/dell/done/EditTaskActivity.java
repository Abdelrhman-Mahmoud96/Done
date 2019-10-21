package com.example.dell.done;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dell.done.AAC.AppExecutors;
import com.example.dell.done.AAC.AppViewModel;
import com.example.dell.done.AAC.AppViewModelFactory;
import com.example.dell.done.Notification.AlarmReceiver;
import com.example.dell.done.Room.AppDatabase;
import com.example.dell.done.Room.TaskEntry;
import com.example.dell.done.Setting.Setting;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditTaskActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private EditText title ;
    private EditText description ;
    private EditText date ;
    private EditText time ;
    private Button saveButton;
    private int DEFAULT_VALUE = -1;
    private int taskId = DEFAULT_VALUE;
    SharedPreferences sharedPreferences ;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm");
    AppDatabase database;
    final Calendar calendar = Calendar.getInstance();
    int hours,minutes;
    Date convertedDate = null;
    Date convertedTime = null;
    boolean isChanged;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        title = findViewById(R.id.editTitle);
        description = findViewById(R.id.editDesc);
        date = findViewById(R.id.editDate);
        time = findViewById(R.id.editTime);
        saveButton = findViewById(R.id.buttonSave);

        database = AppDatabase.getInstance(getApplicationContext());
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);



        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year );
                calendar.set(Calendar.MONTH,month );
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth );
                calenderToString();
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTaskActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });



        final TimePickerDialog timeSetListener = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.setText(hourOfDay+":"+minute );
            }
        },hours,minutes,false);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSetListener.show();
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("taskId")) {
            saveButton.setText("Update");
            setTitle("Edit Task");
            if (taskId == DEFAULT_VALUE) {
                taskId = intent.getIntExtra("taskId",DEFAULT_VALUE);

                AppViewModelFactory addTaskViewModelFactory = new AppViewModelFactory(database,taskId);
                final AppViewModel addTaskViewModel = ViewModelProviders.of(this,addTaskViewModelFactory).get(AppViewModel.class);
                addTaskViewModel.getTaskEntryLiveData().observe(this, new Observer<TaskEntry>() {
                    @Override
                    public void onChanged(TaskEntry taskEntry) {
                        addTaskViewModel.getTaskEntryLiveData().removeObserver(this);
                        bindDataToView(taskEntry);
                    }
                });


            }
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(title.getText().toString().trim()) ||
                        TextUtils.isEmpty(description.getText().toString().trim()) ||
                        TextUtils.isEmpty(date.getText().toString().trim())||
                        TextUtils.isEmpty(time.getText().toString().trim()))
                { Toast.makeText(EditTaskActivity.this,"some info.s are empty" ,Toast.LENGTH_LONG )
                        .show(); }
                else
                {   insertOrUpdateTask();
                    finish(); }
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            changeTextColor(sharedPreferences);
        }

        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isChanged = true;
                return false;
            }
        };

        title.setOnTouchListener(onTouchListener);
        description.setOnTouchListener(onTouchListener);
        date.setOnTouchListener(onTouchListener);
        time.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onBackPressed() {
        if(isChanged == false)
        {
            super.onBackPressed();
            return;
        }
        else
        {
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            };
            showDialog(onClickListener);
        }

    }

    private void showDialog (DialogInterface.OnClickListener dialogInterface)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit")
                .setMessage("are you sure want to exit ?")
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dialog!= null)
                        {
                            dialog.dismiss();
                        }
                    }
                })
                .setPositiveButton("yes", dialogInterface);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent  = new Intent (EditTaskActivity.this, Setting.class);
            startActivity(settingIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void bindDataToView(TaskEntry taskEntry)
    {

        title.setText(taskEntry.getTitle());
        description.setText(taskEntry.getDescription() );


        String dateText = dateFormat.format(taskEntry.getDate());
        date.setText(dateText );


        String timeText = timeFormat.format(taskEntry.getTime());
        time.setText(timeText);
    }

    private void insertOrUpdateTask()
    {
        final String titleText = title.getText().toString();
        final String descText = description.getText().toString();
        String dateText = date.getText().toString();
        String timeText = time.getText().toString();

//        String dateFull = dateText +" "+ timeText;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");


        try {
//              convertedFullDate = simpleDateFormat.parse(dateFull);
              convertedDate = dateFormat.parse(dateText);
              convertedTime = timeFormat.parse(timeText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Integer> timeList = convertTimeToList(timeText);
        List<Integer> dateList = convertDateToNumbers(convertedDate);
        final Calendar calendar1 = Calendar.getInstance();


        calendar1.set(dateList.get(2) + 2000,dateList.get(1) - 1 ,dateList.get(0) ,timeList.get(0) ,timeList.get(1) );



        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            TaskEntry entry = new TaskEntry(titleText,descText,convertedDate,convertedTime);

            @Override
            public void run() {
                if(taskId == DEFAULT_VALUE)
                {
                    database.taskDao().insertTask(entry);
                    startAlarm(entry.getId(),entry.getTitle(),calendar1);
                }
                else
                {
                    entry.setId(taskId);
                    database.taskDao().updateTask(entry);
                    startAlarm(entry.getId(),entry.getTitle(),calendar1);
                }

            }
        });


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changeTextColor(SharedPreferences sharedPreferences) {

        String color = sharedPreferences.getString(getString(R.string.color_array_key),getString(R.string.blackValue ));
        if(color.equals("Red"))
        {
            title.setTextColor(getResources().getColor(R.color.red,null) );
            description.setTextColor(getResources().getColor(R.color.red,null));
            date.setTextColor(getResources().getColor(R.color.red,null) );
            time.setTextColor(getResources().getColor(R.color.red,null) );
        }
        if(color.equals("Blue"))
        {
            title.setTextColor(getResources().getColor(R.color.blue,null) );
            description.setTextColor(getResources().getColor(R.color.blue,null) );
            date.setTextColor(getResources().getColor(R.color.blue,null) );
            time.setTextColor(getResources().getColor(R.color.blue,null) );
        }
        if(color.equals("Green"))
        {
            title.setTextColor(getResources().getColor(R.color.green,null) );
            description.setTextColor(getResources().getColor(R.color.green,null) );
            date.setTextColor(getResources().getColor(R.color.green,null) );
            time.setTextColor(getResources().getColor(R.color.green,null) );
        }
        if(color.equals("Black"))
        {
            title.setTextColor(getResources().getColor(R.color.black,null) );
            description.setTextColor(getResources().getColor(R.color.black,null) );
            date.setTextColor(getResources().getColor(R.color.black,null) );
            time.setTextColor(getResources().getColor(R.color.black,null) );
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.color_array_key)))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                changeTextColor(sharedPreferences);
            }
        }

    }

    private void calenderToString()
    {
        String dateText = dateFormat.format(calendar.getTime());
        date.setText(dateText,null );
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

    private List<Integer> convertTimeToList(String time)
    {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
//        String timeString = simpleDateFormat.format(time);
        String[] dateList = time.split(":");
        List<Integer> dateNumbers = new ArrayList<>();

        for(int i = 0;i < dateList.length;i++)
        {
            dateNumbers.add(Integer.parseInt(dateList[i]));
        }

        return dateNumbers;
    }

    private void startAlarm(int id ,String title,Calendar calendar)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);

        intent.putExtra("taskId",id+1);
        intent.putExtra("taskTitle",title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,400,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
        }
    }
/*
*
* Draw lines for description edit text*/

    public static class LinedEditText extends android.support.v7.widget.AppCompatEditText {
        private Rect mRect;
        private Paint mPaint;
        // we need this constructor for LayoutInflater
        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);

            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0x800000FF);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int count = getLineCount();
            Rect r = mRect;
            Paint paint = mPaint;
            for (int i = 0; i < count; i++) {
                int baseline = getLineBounds(i, r);
                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            }
            super.onDraw(canvas);
        }
    }

}
