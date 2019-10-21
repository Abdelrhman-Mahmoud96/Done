package com.example.dell.done.Adapters;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.done.R;
import com.example.dell.done.Room.TaskEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>  {

    private static final String DATE_FORMAT = "dd/MM/yyy";
    RecyclerViewItemClickListner onItemClick;
    List<TaskEntry> taskEntries  = new ArrayList<>();
    Context context;
    TextView title;
    TextView date;
    TextView state;
    boolean clicked;

    public RecyclerViewAdapter(Context context ,RecyclerViewItemClickListner onItemClick)
    {
        this.context = context;
        this.onItemClick = onItemClick;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(context).inflate(R.layout.task_item,parent,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.bindView(taskEntries.get(position));
    }


    public void setTasks(List<TaskEntry> taskEntrie) {
        taskEntries = taskEntrie;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

        }


        public void bindView(TaskEntry taskEntry)
        {
             title = itemView.findViewById(R.id.taskTitle);
             date = itemView.findViewById(R.id.taskDate);
             state = itemView.findViewById(R.id.taskState);

            title.setText(taskEntry.getTitle());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateText  = simpleDateFormat.format(taskEntry.getDate());
            date.setText(dateText);

            List<Integer> dateList = convertDateToNumbers(taskEntry.getDate());
            Calendar calendar = Calendar.getInstance();
            int year =  calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            if(year == dateList.get(2) + 2000 && month == dateList.get(1) -1 && day == dateList.get(0))
            {
                state.setBackgroundColor(context.getResources().getColor(R.color.green) );
            }
            else if (year <= dateList.get(2) + 2000 && month <= dateList.get(1) - 1 && day < dateList.get(0))
            {
                        state.setBackgroundColor(context.getResources().getColor(R.color.gray) );

            }
            else
            {
                state.setBackgroundColor(context.getResources().getColor(R.color.red) );
            }


        }

        @Override
        public void onClick(View v) {

            onItemClick.onClick(getAdapterPosition());
            int finalRadius = (int) Math.hypot(v.getWidth()/2,v.getHeight()/2);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(v,v.getWidth()/2,v.getHeight()/2, 0, finalRadius);
                v.setBackgroundColor(context.getResources().getColor(R.color.colorAccent ));
                title.setTextColor(context.getResources().getColor(android.R.color.white));
                date.setTextColor(context.getResources().getColor(android.R.color.white));
                animator.start();

                 }
        }


    }

    @Override
    public int getItemCount() {

        return taskEntries.size();
    }

    public List<TaskEntry> getTaskEntries() {
        return taskEntries;
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
}


