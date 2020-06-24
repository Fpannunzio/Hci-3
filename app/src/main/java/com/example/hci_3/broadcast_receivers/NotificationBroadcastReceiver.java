package com.example.hci_3.broadcast_receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hci_3.MainActivity;
import com.example.hci_3.R;
import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private Gson gson;
    private SharedPreferences storedDevicesSP, notificationIDsSP;
    private NotificationManagerCompat notificationManager;
    Map<String, Device> storedDevices, newDevices;
    Map<String,Integer> notificationIDs;
    Integer lastNotificationID;
    Context context;
    ExecutorService executorService;

    public NotificationBroadcastReceiver() {
        super();
        gson = new GsonBuilder()
                .registerTypeAdapter(Device.class, new DeviceDeserializer())
                .create();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean notificationsActive = context.getSharedPreferences("settings",
                Context.MODE_PRIVATE).getBoolean("notifications", true);

        if(!notificationsActive)
            return;

        init(context);
        ApiClient.getInstance().getDevices(list -> executorService.execute(() -> handleApiRequest(list)), (m, c) -> Log.w("uncriticalError", "NBR - Failed to get devices: " + m + " Code: " + c));
    }

    @SuppressWarnings("unchecked")  //Solo se guardan integer. (Notification IDs)
    private void init(Context context) {
        notificationManager = NotificationManagerCompat.from(context);

        storedDevicesSP = context.getSharedPreferences(context.getString(R.string.stored_devices_SP_key), Context.MODE_PRIVATE);

        storedDevices = storedDevicesSP.getAll().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> gson.fromJson((String) e.getValue(), Device.class)));

        notificationIDsSP = context.getSharedPreferences(context.getString(R.string.notification_IDs_SP_key), Context.MODE_PRIVATE);

        notificationIDs = (Map<String, Integer>) notificationIDsSP.getAll();

        if((lastNotificationID = notificationIDs.get(context.getString(R.string.last_notification_ID))) == null )
            lastNotificationID = 0;

        newDevices = new HashMap<>();

        this.context = context;
    }

    private void handleApiRequest(List<Device> devices) {

        for(Device device: devices){

            Device olderDev = storedDevices.get(device.getId());

            if( olderDev == null ) {
                emitNotification(device,context.getString(R.string.notifications_device_added, device.getParsedName()),"device.added");
                newDevices.put(device.getId(),device);
            } else {
                final Map<String, String> comparision = olderDev.compareToNewerVersion(device);

                for(Map.Entry<String,String> change : comparision.entrySet()) {
                    int eventID;
                    if(change.getKey().startsWith("state")){
                        String[] aux = change.getKey().split("\\.");
                        eventID = context.getResources().getIdentifier(aux[aux.length - 1],"string", context.getPackageName());
                    } else{
                        eventID = context.getResources().getIdentifier(change.getKey(),"string", context.getPackageName());
                    }
                    emitNotification(device, context.getString(R.string.notifications_device_stated_changed, context.getString(eventID), change.getValue()), change.getKey());
                }

                if(!comparision.isEmpty()){
                    emitSummary(device);
                    newDevices.put(device.getId(),device);
                }
            }
        }

        List<String> newDevIDs = devices.stream().map(Device::getId).collect(Collectors.toList());

        for(Device storedDevice : storedDevices.values()){
            if(!newDevIDs.contains(storedDevice.getId())){
                emitNotification(storedDevice,context.getString(R.string.notifications_device_deleted, storedDevice.getParsedName()), "device.deleted");
                newDevices.put(storedDevice.getId(),null);
            }
        }

        finish();
    }

    private void emitSummary(Device device) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, getChannelID(device))
                .setSmallIcon(R.drawable.smartify_logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.InboxStyle()
                    .setSummaryText(device.getParsedName()))
                .setGroupSummary(true)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(getIntent(device))
                .setAutoCancel(true)
                .setGroup(device.getId())
                .setOnlyAlertOnce(true);

        notificationManager.notify(getNotificationID(device.getId(),"summary"), builder.build());
    }

    private void finish() {

        storeDevicesToSP();

        storeNotificationIDsToSP();
    }

    private void storeNotificationIDsToSP() {
        notificationIDs.put(context.getString(R.string.last_notification_ID), lastNotificationID);

        SharedPreferences.Editor preferencesEditor = notificationIDsSP.edit();
        notificationIDs.entrySet().forEach(e->preferencesEditor.putInt(e.getKey(),e.getValue()));
        preferencesEditor.apply();
    }

    private void storeDevicesToSP() {
        SharedPreferences.Editor preferencesEditor = storedDevicesSP.edit();

        for (Map.Entry<String,Device> entry : newDevices.entrySet()){
            if(entry.getValue() == null){
                preferencesEditor.remove(entry.getKey());
            } else {
                preferencesEditor.putString(entry.getKey(), gson.toJson(entry.getValue()));
            }
        }

        preferencesEditor.apply();
    }

    private Map<String, Device> getSharedPreferences(SharedPreferences preferences){
        return preferences.getAll().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> gson.fromJson((String) e.getValue(), Device.class)));
    }

    private Integer getNotificationID(String deviceID, String event){
        String notificationIDKey = deviceID + "." + event;
        Integer notificationID = notificationIDs.get(notificationIDKey);
        if(notificationID == null) {
            notificationID = lastNotificationID++;
            notificationIDs.put(notificationIDKey, notificationID);
        }
        return notificationID;
    }

    private PendingIntent getIntent(Device device){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction("notifications"); //TODO: pasar a R.Strings
        intent.putExtra("roomID", device.getRoom().getId());
        intent.putExtra("roomName", device.getRoom().getParsedName());
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private void emitNotification(Device device, String message, String event){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, getChannelID(device))
                .setSmallIcon(R.drawable.smartify_logo)
                .setContentTitle(device.getParsedName())
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(getIntent(device))
                .setAutoCancel(true)
                .setGroup(device.getId())
                .setOnlyAlertOnce(true);

        notificationManager.notify(getNotificationID(device.getId(),event), builder.build());
    }

    private String getChannelID(Device device) {
        return device.isFav() ? context.getString(R.string.notifications_favorite_channel_ID) : context.getString(R.string.notifications_standar_channel_ID);
    }
}