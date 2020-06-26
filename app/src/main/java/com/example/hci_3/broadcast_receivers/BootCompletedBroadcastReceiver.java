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

import com.example.hci_3.MainActivity;
import com.example.hci_3.R;


public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        intent.getAction();

        setAlarm(context);

        createNotificationChannel(context);
    }

    private void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            CharSequence name = context.getString(R.string.notifications_channel_name);
            String description = context.getString(R.string.notifications_channel_description);
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.notifications_standar_channel_ID), name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

            CharSequence nameFavorite = context.getString(R.string.notifications_favorite_channel_name);
            String descriptionFavorite = context.getString(R.string.notifications_favorite_channel_description);
            NotificationChannel channelFavorite = new NotificationChannel(context.getString(R.string.notifications_favorite_channel_ID), nameFavorite, importance);
            channel.setDescription(descriptionFavorite);
            notificationManager.createNotificationChannel(channelFavorite);
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
    }
}
