package com.example.hci_3.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.stream.Collectors;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private Gson gson;

    public NotificationBroadcastReceiver() {
        super();
        gson = new GsonBuilder()
                .registerTypeAdapter(Device.class, new DeviceDeserializer())
                .create();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("pruebaBroadcast", "Notification Broadcast");
        SharedPreferences preferences = context.getSharedPreferences("tobias", Context.MODE_PRIVATE);
        Map<String, Device> storedDevices = preferences.getAll().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> gson.fromJson((String) e.getValue(), Device.class)));
        Log.v("devices", storedDevices.toString());
    }
}
