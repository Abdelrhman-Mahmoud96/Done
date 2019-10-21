package com.example.dell.done.Room;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


@Entity (tableName = "tasks")
public class TaskEntry {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Date date;
    private Date time;

    @Ignore
    public TaskEntry(String title,String description, Date date,Date time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;

    }

    public TaskEntry(int id,String title ,String description,Date date,Date time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public int  getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
