package com.example.hci_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import android.view.Menu;

import com.example.hci_3.fragments.FavoritesFragmentDirections;
import com.example.hci_3.repositories.DeviceRepository;
import com.example.hci_3.view_models.ActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
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

        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color.appBackgroundColor));

        boolean isNightModeOn = getSharedPreferences(getString(R.string.settingsFile), Context.MODE_PRIVATE).getBoolean(getString(R.string.night_mode_boolean), false);

        if (isNightModeOn)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        setSupportActionBar(toolbar);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        Intent intent = getIntent();

        if(intent.getAction() != null && intent.getAction().equals("notifications")) {

            Bundle extras = intent.getExtras();
            String roomName = extras.getString("roomName");
            String roomId = extras.getString("roomID");
            String homeName = extras.getString("homeName");

            navController.navigate(FavoritesFragmentDirections.actionFavoritosToRoom(Objects.requireNonNull(roomId), Objects.requireNonNull(roomName), Objects.requireNonNull(homeName)));
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

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setAlarm(){
        boolean alarmUp = PendingIntent.getBroadcast(this, 0,
                new Intent(ACTION_ALARM), PendingIntent.FLAG_NO_CREATE) != null;

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
    }

    public static class AlarmHandlerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
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