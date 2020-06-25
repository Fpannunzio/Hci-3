package com.example.hci_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;

import com.example.hci_3.repositories.DeviceRepository;
import com.example.hci_3.view_models.ActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    //private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    public static final String ACTION_ALARM = "com.example.hci_3.ALARM";
    public static final String ACTION_ALARM_HANDLE = "com.example.hci_3.ALARM_HANDLE";
    public static final int INTERVAL = 60000;

    BroadcastReceiver dataSyncBroadcastReceiver;
    ActivityViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(ActivityViewModel.class);

        model.startPollingDevices();

        DeviceRepository.getInstance().updateDevices();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        setSupportActionBar(toolbar);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Intent intent = getIntent();
        Log.v("notif1", "estoy aca");
        if(intent.getAction() != null && intent.getAction().equals("notifications")) {
            Log.v("notif2", "estoy aca2");
            String roomName = intent.getStringExtra("roomName");
            String roomId = intent.getStringExtra("roomID");
            Log.v("homename", roomName);
            //String homeName = intent.getStringExtra("homeName");
            //navController.navigate(FavoritesFragmentDirections.actionFavoritosToRoom(Objects.requireNonNull(roomId), Objects.requireNonNull(roomName), Objects.requireNonNull(homeName)));
        }

        dataSyncBroadcastReceiver = new KillNotificationBroadcastReceiver();

        setAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();

        model.startPollingDevices();
        IntentFilter filter = new IntentFilter(ACTION_ALARM_HANDLE);
        filter.setPriority(2);
        registerReceiver(dataSyncBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        model.stopPollingDevices();
        unregisterReceiver(dataSyncBroadcastReceiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setAlarm(){
        boolean alarmUp = PendingIntent.getBroadcast(this, 0,
                new Intent(ACTION_ALARM), PendingIntent.FLAG_NO_CREATE) != null;

        Log.v("pruebabroadcast", String.valueOf(alarmUp));

        if(alarmUp)
            return;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent notificationReceiverIntent = new Intent(MainActivity.this, AlarmHandlerBroadcastReceiver.class);
        notificationReceiverIntent.setAction(ACTION_ALARM);

        PendingIntent notificationReceiverPendingIntent =
                PendingIntent.getBroadcast(this, 0, notificationReceiverIntent, 0);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INTERVAL,
                INTERVAL,
                notificationReceiverPendingIntent);

        Log.d("pruebaBroadcast", "Single alarm set on:" + DateFormat.getDateTimeInstance().format(new Date()));
    }

    public static class AlarmHandlerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("pruebaBroadcast", "alarmHandlerBroadcast");
            Intent newIntent = new Intent(MainActivity.ACTION_ALARM_HANDLE);
            newIntent.setPackage(context.getPackageName());
            context.sendOrderedBroadcast(newIntent, null);
        }
    }

    public static class KillNotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
        }
    }
}