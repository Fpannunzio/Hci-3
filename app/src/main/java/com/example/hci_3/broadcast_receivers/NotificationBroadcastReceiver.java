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
    private boolean favoriteDevicesNotifications, defaultDevicesNotifications;
    private SharedPreferences storedDevicesSP;
    private SharedPreferences notificationIDsSP;
    private NotificationManagerCompat notificationManager;
    private Map<String, Device> storedDevices, newDevices;
    private Map<String,Integer> notificationIDs;
    private ExecutorService executorService;
    private String favoriteChannelID, defaultChannelID;
    private Integer lastNotificationID;
    private Context context;
    private Gson gson;

    public NotificationBroadcastReceiver() {
        super();
        gson = new GsonBuilder()
                .registerTypeAdapter(Device.class, new DeviceDeserializer())
                .create();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences settingsSP = context.getSharedPreferences(context.getString(R.string.settingsFile), Context.MODE_PRIVATE);

        boolean notificationsActive = settingsSP.getBoolean(String.valueOf(R.id.allNotificationsSwitch), true);

        favoriteDevicesNotifications = settingsSP.getBoolean(String.valueOf(R.id.favoriteNotificationsSwitch), true);

        defaultDevicesNotifications = settingsSP.getBoolean(String.valueOf(R.id.defaultNotificationsSwitch), true);

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

        favoriteChannelID = context.getString(R.string.notifications_favorite_channel_ID);
        defaultChannelID = context.getString(R.string.notifications_standar_channel_ID);

        this.context = context;
    }

    private void handleApiRequest(List<Device> devices) {

        for(Device device: devices){
            if(validateSendNotification(device)){
                Device olderDev = storedDevices.get(device.getId());

                if( olderDev == null ) {
                    emitNotification( device, getMessage(device,"device.added",null),"device.added");
                    newDevices.put(device.getId(),device);
                } else {
                    final Map<String, String> comparision = olderDev.compareToNewerVersion(device);

                    for(Map.Entry<String,String> change : comparision.entrySet()) {
                        emitNotification(device, getMessage(device,change.getKey(),change.getValue()), change.getKey());
                    }

                    if(!comparision.isEmpty()){
                        emitSummary(device);
                        newDevices.put(device.getId(), device);
                    }
                }
            }
        }

        List<String> newDevIDs = devices.stream().map(Device::getId).collect(Collectors.toList());

        for(Device storedDevice : storedDevices.values()){
            if(validateSendNotification(storedDevice)){
                if(!newDevIDs.contains(storedDevice.getId())){
                    emitNotification(storedDevice,getMessage(storedDevice,"device.deleted",null), "device.deleted");
                    newDevices.put(storedDevice.getId(),null);
                }
            }
        }
        finish();
    }

    private void emitNotification(Device device, String message, String event){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, getChannelID(device))
                .setSmallIcon(R.drawable.smartify_logo)
                .setContentTitle(device.getParsedName())
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getIntent(device))
                .setAutoCancel(true)
                .setGroup(device.getId())
                .setOnlyAlertOnce(true);

        notificationManager.notify(getNotificationID(device.getId(),event), builder.build());
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

    private boolean validateSendNotification(Device device) {
        if(device.isFav())
            return favoriteDevicesNotifications;
        return defaultDevicesNotifications;
    }

    private String getEvent(Map.Entry<String,String> change){
        String event;
        int eventID;
        if(change.getKey().startsWith("state")){
            String[] aux = change.getKey().split("\\.");
            eventID = context.getResources().getIdentifier(aux[aux.length - 1],"string", context.getPackageName());
        } else{
            eventID = context.getResources().getIdentifier(change.getKey(),"string", context.getPackageName());
        }

        if(eventID == 0)
            event = change.getKey();
        else
            event = context.getString(eventID);
        return event;
    }

    private String getValue(Map.Entry<String,String> change){
        String value;
        int valueID = context.getResources().getIdentifier(change.getValue(),"string", context.getPackageName());

        if(!change.getValue().matches("-?[0-9]+") && valueID != 0)
            value = context.getString(valueID);
        else
            value = change.getValue();
        return value;
    }

    private String getMessage(Device device, String event, String value){
        if(event.matches("meta.fav")){
            if(Boolean.parseBoolean(value))
                return context.getString(R.string.notificactions_message_fav_true);
            else
                return context.getString(R.string.notificactions_message_fav_false);
        }
        if(!event.startsWith("state")){
            int messageID = context.getResources().getIdentifier("notificactions_message_" + event,"string", context.getPackageName());
            if(event.matches("(device\\.deleted|device\\.added)"))
                return context.getString(messageID);
            return context.getString(messageID,value);
        } else {
            String[] aux = event.split("\\.");

            int messageID = context.getResources().getIdentifier(aux[aux.length - 1],"string", context.getPackageName());
            int valueID = context.getResources().getIdentifier(value,"string", context.getPackageName());

            String state = context.getString(messageID);

            if(!value.matches("-?[0-9]+") && valueID != 0)
                value = context.getString(valueID);

            return context.getString(R.string.notificactions_message_device_stated_changed, state, value);
        }
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
        intent.putExtra("homeName", device.getRoom().getHome().getName());
        return PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
    }

    private String getChannelID(Device device) {
        return device.isFav() ? favoriteChannelID : defaultChannelID;
    }

    private void finish() {

        storeDevicesToSP();

        storeNotificationIDsToSP();
    }

    private void storeNotificationIDsToSP() {
        notificationIDs.put(context.getString(R.string.last_notification_ID), lastNotificationID);

        SharedPreferences.Editor preferencesEditor = notificationIDsSP.edit();
        notificationIDs.entrySet().forEach(e -> preferencesEditor.putInt(e.getKey(), e.getValue()));
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
}