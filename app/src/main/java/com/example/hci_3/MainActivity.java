package com.example.hci_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
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
import android.view.MenuItem;


import com.example.hci_3.repositories.DeviceRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    //private AppBarConfiguration mAppBarConfiguration;

    public static final String ACTION_ALARM_HANDLE = "com.example.hci_3.ALARM_HANDLE";

    BroadcastReceiver dataSyncBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar =  findViewById(R.id.toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setSupportActionBar(toolbar);

       //mAppBarConfiguration = new AppBarConfiguration().Builder(R.id.favoritos, R.id.hogares, R.id.rutinas).build();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        dataSyncBroadcastReceiver = new DataSyncBroadcastReceiver();

        setAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(ACTION_ALARM_HANDLE);
        filter.setPriority(2);
        registerReceiver(dataSyncBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(dataSyncBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                // perform query here
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void setAlarm(){
        final int INTERVAL = 60000;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent notificationReceiverIntent = new Intent(MainActivity.this, AlarmHandlerBroadcastReceiver.class);
        notificationReceiverIntent.setAction(ACTION_ALARM_HANDLE);

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

    public static class DataSyncBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("pruebaBroadcast", "dataSyncBroadcast");

            DeviceRepository.getInstance().updateDevices();

            abortBroadcast();
        }
    }
}