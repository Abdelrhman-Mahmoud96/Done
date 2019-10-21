package com.example.dell.done.Room;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date timespanToDate (Long time)
    {
        if(time != null)
        {
           Date date = new Date(time);
           return date;
        }
        return null;
    }

    @TypeConverter
    public static Long dateToTimspan (Date time)
    {
        if(time != null)
        {
            Long date = time.getTime();
            return date;
        }
        return null;
    }
}
