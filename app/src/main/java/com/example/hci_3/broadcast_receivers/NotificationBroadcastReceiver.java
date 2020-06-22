package com.example.hci_3.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hci_3.R;
import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.LogEntry;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
//    private static final String LOGS_REQUESTED = "5";
//    private static final int MAX_LOG_COUNT = 100;
//    private int logsProcessed = 0;
//    private List<LogEntry> logs;
//    private Date lastDate = new Date(Long.MIN_VALUE);
//    private ApiClient api = ApiClient.getInstance();
//    private SharedPreferences sharedPref;
//    private Context vContext;
//    @Override
    public void onReceive(Context context, Intent intent) {
//        vContext = context;
//        sharedPref = context.getSharedPreferences(context.getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
//        String defaultValue = new Date(Long.MIN_VALUE).toString();
//        String lastDateString = sharedPref.getString(context.getString(R.string.notifications), defaultValue);
//        lastDate = new Date(lastDateString);
//        logs = new ArrayList<>();
//
//        api.getLogs( "", "", this::logSuccessHandler, (m, c) -> Log.w("uncriticalError", "Failed to get logs: " + m + " Code: " + c));
//
//
////        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.sharedPreferences), Context.MODE_PRIVATE);
////        SharedPreferences.Editor editor = sharedPref.edit();
////        editor.putInt(context.getString(R.string.notifications), newHighScore);
////        editor.apply();
//
//        Log.d("pruebaBroadcast", "Prueba Alarm at: " + DateFormat.getDateTimeInstance().format(new Date()));
    }
//
//    private void logSuccessHandler(List<LogEntry> ans){
//         ans = ans.stream().filter(entry -> lastDate.before(entry.getTimestamp())).collect(Collectors.toList());
//
//        logs.addAll(ans);
//        Log.d("notification",ans.toString());
//        logsProcessed += ans.size();
//        if(logsProcessed <= MAX_LOG_COUNT && logs.get(logs.size() - 1).getTimestamp().after(lastDate))
//            api.getLogs( "", "", this::logSuccessHandler, (m, c) -> Log.w("uncriticalError", "Failed to get logs: " + m + " Code: " + c));
//        else{
//            Log.d("notification",logs.toString());
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString(vContext.getString(R.string.notifications), logs.get(0).getTimestamp().toString());
//            editor.apply();
//        }
//    }
}
