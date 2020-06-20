package com.example.hci_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FavoriteDeviceViewModel model = new ViewModelProvider(this).get(FavoriteDeviceViewModel.class);

        DeviceAdapter adapter = new DeviceAdapter();
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv.setAdapter(adapter);

        model.getDevices().observe(this, adapter::setDevices);

        DeviceRepository.getInstance().getDevices();
    }
}