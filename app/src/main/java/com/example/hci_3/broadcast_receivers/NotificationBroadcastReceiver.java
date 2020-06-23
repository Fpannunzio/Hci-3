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
import java.util.stream.Collectors;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private Gson gson;
    SharedPreferences preferences;
    private static int nID = 0;

    public NotificationBroadcastReceiver() {
        super();
        gson = new GsonBuilder()
                .registerTypeAdapter(Device.class, new DeviceDeserializer())
                .create();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("pruebaBroadcast", "Notification Broadcast");

        updateSharedPreferences(context.getSharedPreferences("tobias", Context.MODE_PRIVATE));

        Map<String, Device> storedDevices = getSharedPreferences(preferences);

        ApiClient.getInstance().getDevices(l -> handleApiRequest(l,storedDevices, context), (m, c) -> Log.w("uncriticalError", "NBR - Failed to get devices: " + m + " Code: " + c));
    }

    private void handleApiRequest(List<Device> devices, Map<String, Device> storedDevices, Context context) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        SharedPreferences notificationsSP = context.getSharedPreferences("notificationIDs", Context.MODE_PRIVATE);

        @SuppressWarnings("unchecked")  //Solo se gaurdan integer. (Notification IDs)
        Map<String,Integer> notificationIDsMap = (Map<String, Integer>) notificationsSP.getAll();

        Map<String, Device> newDevices = new HashMap<>();

        for(Device device: devices){
            Device olderDev = storedDevices.get(device.getId());
            if( olderDev != null ) {

                final Map<String, String> comparision = olderDev.compareToNewerVersion(device);

                for(Map.Entry<String,String> change : comparision.entrySet()) {

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setAction("notifications");
                intent.putExtra("roomID",device.getRoom().getId());
                intent.putExtra("roomName",device.getRoom().getParsedName());
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                            .setSmallIcon(R.drawable.smartify_logo)
                            .setContentTitle(device.getParsedName())
                            .setContentText("Se modifico "  + change.getKey() + " ahora el valor es: " + change.getValue() )
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            // Set the intent that will fire when the user taps the notification
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setGroup(device.getId())
                            .setOnlyAlertOnce(true);
                    //TODO:ENcapsualr
                    Integer notificationID = notificationIDsMap.get(device.getId()+change.getKey());
                    if(notificationID == null) {
                        notificationID = nID++;
                        notificationIDsMap.put(device.getId()+change.getKey(), notificationID);
                    }
                    notificationManager.notify(notificationID, builder.build());
                }
                if(!comparision.isEmpty()){
                    newDevices.put(device.getId(),device);
                }
            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                        .setSmallIcon(R.drawable.smartify_logo)
                        .setContentTitle("Nuevo dispositivo")
                        .setContentText(device.getParsedName() + "fue agregado")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        //                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setGroup(device.getId())
                        .setOnlyAlertOnce(true);

                notificationManager.notify(nID++, builder.build());

                newDevices.put(device.getId(),device);
            }
            newDevices.put(device.getId(),device);
        }
        List<String> newDevIDs = devices.stream().map(Device::getId).collect(Collectors.toList());
        for(Device storedDevice : storedDevices.values()){
            if(!newDevIDs.contains(storedDevice.getId())){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.channel_id))
                        .setSmallIcon(R.drawable.smartify_logo)
                        .setContentTitle("Dispositivo eliminado")
                        .setContentText(storedDevice.getParsedName() + "fue eliminado")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        //                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setGroup(storedDevice.getId())
                        .setOnlyAlertOnce(true);

                notificationManager.notify(nID++, builder.build());

                newDevices.put(storedDevice.getId(),null);
            }
        }

        saveSharedPreferences(preferences, newDevices);

        SharedPreferences.Editor preferencesEditor = notificationsSP.edit();
        notificationIDsMap.entrySet().forEach(e->preferencesEditor.putInt(e.getKey(),e.getValue()));
        preferencesEditor.apply();
    }

    private void saveSharedPreferences(SharedPreferences preferences, Map<String,Device> newDevice) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        for (Map.Entry<String,Device> entry : newDevice.entrySet()){
            if(entry.getValue() == null){
                preferencesEditor.remove(entry.getKey());
            } else {
                preferencesEditor.putString(entry.getKey(), gson.toJson(entry.getValue()));
            }
        }

        preferencesEditor.apply();
    }

    private void updateSharedPreferences(SharedPreferences newPreferences) {
        preferences = newPreferences;
    }

    private Map<String, Device> getSharedPreferences(SharedPreferences preferences){
        return preferences.getAll().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> gson.fromJson((String) e.getValue(), Device.class)));
    }
}
//comparision.keySet().stream().map(k -> k + " " + comparision.get(k) + "|").reduce(" ", String::concat)