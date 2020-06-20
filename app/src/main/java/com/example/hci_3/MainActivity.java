package com.example.hci_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.widget.Toast;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    DeviceAdapter adapter;
    List<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devices = new ArrayList<>();
        adapter = new DeviceAdapter(devices);
        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SpacesItemDecoration(30));




        ApiClient.getInstance().getDevices((devices) -> {

            this.devices.addAll(devices);
            adapter.notifyDataSetChanged();

        }, this::handleError);
    }

    private void handleError(String message, int code){
        String text = getResources().getString(R.string.error_message, message, code);
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }
}