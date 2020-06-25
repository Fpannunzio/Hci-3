package com.example.hci_3.broadcast_receivers;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import com.example.hci_3.MainActivity;
import com.example.hci_3.R;

import java.text.DateFormat;
import java.util.Date;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.getAction();//TODO send help
        setAlarm(context);
        createNotificationChannel(context);
        Log.v("BCBR", "Llegue al broadcast reciever");
    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notifications_channel_name);
            String description = context.getString(R.string.notifications_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.notifications_standar_channel_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            name = "Favoritos";
            description = "Mensajes para los dispositivios facoritos";
            channel = new NotificationChannel(context.getString(R.string.notifications_favorite_channel_ID), name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setAlarm(Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationReceiverIntent = new Intent(context, MainActivity.AlarmHandlerBroadcastReceiver.class);
        notificationReceiverIntent.setAction(MainActivity.ACTION_ALARM);

        PendingIntent notificationReceiverPendingIntent =
                PendingIntent.getBroadcast(context, 0, notificationReceiverIntent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + MainActivity.INTERVAL,
                MainActivity.INTERVAL,
                notificationReceiverPendingIntent);

        Log.d("pruebaBroadcast", "Single alarm set on:" + DateFormat.getDateTimeInstance().format(new Date()));
    }
}
