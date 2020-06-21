package com.example.hci_3.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("pruebaBroadcast", "Prueba Alarm at: " + DateFormat.getDateTimeInstance().format(new Date()));
    }
}
