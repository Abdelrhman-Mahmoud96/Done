package com.example.dell.done.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.dell.done.MainActivity;
import com.example.dell.done.R;
import com.example.dell.done.Room.TaskEntry;
import com.example.dell.done.Service.DeleteTask;
import com.example.dell.done.Service.ServiceManager;

import java.io.Serializable;

public class  NotificationUtils  {


    private static String NOTIFICATION_CHANEL_ID = "taskChanel";
    private static String NOTIFICATION_CHANEL_NAME = "taskDay";
    private static String NOTIFICATION_BUILDER_ID = "task";
    private static int NOTIFICATION_ID = 2;

    public static void taskReminderNotification(Context context,int id , String title)
    {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANEL_ID, NOTIFICATION_CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_BUILDER_ID)
                .setColor(ContextCompat.getColor(context,R.color.colorPrimary ))
                .setChannelId(NOTIFICATION_CHANEL_ID)
                .setSmallIcon(R.drawable.noticon) //Todo change the icon here
                .setContentTitle("Done")
                .setContentText("don't forget to "+ title)
                .addAction(notificationGrant(context,title))
                .addAction(notificationDismiss(context))
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        manager.notify(NOTIFICATION_ID,notificationBuilder.build() );

    }

    private static NotificationCompat.Action notificationDismiss(Context context) {
        Intent intent = new Intent(context, ServiceManager.class);
        intent.putExtra("action",DeleteTask.ACTION_DISMISSED );
        PendingIntent pendingIntent = PendingIntent.getService(context,201,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action dismiss = new NotificationCompat.Action(R.drawable.baseline_clear_black_18dp,"not now",pendingIntent);

        return dismiss;
    }

    private static NotificationCompat.Action notificationGrant(Context context,String title) {

        Intent intent = new Intent(context, ServiceManager.class);
        intent.putExtra("action",DeleteTask.ACTION_GRANTED );
        intent.putExtra("title", title);
        PendingIntent pendingIntent = PendingIntent.getService(context,200,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action grant = new NotificationCompat.Action(R.drawable.baseline_done_black_18dp,"i finished it",pendingIntent);

        return grant;
    }

    public static void notificationClear(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static PendingIntent contentIntent(Context context)
    {
        Intent intent = new Intent(context,MainActivity.class);
       PendingIntent pendingIntent =  PendingIntent.getActivity(context,300,intent,PendingIntent.FLAG_UPDATE_CURRENT );
       return  pendingIntent;
    }

}
