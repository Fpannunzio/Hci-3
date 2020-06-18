package com.example.hci_3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hci_3.api.ApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ApiClient.getInstance().getDevices((devices) -> {
//
//            DeviceAdapter adapter = new DeviceAdapter(devices);
//            rv = findViewById(R.id.recyclerView);
//            rv.setAdapter(adapter);
//            rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//        }, this::handleError);

        List<String> params = new ArrayList<>();
        params.add("fan");

        ApiClient.getInstance().executeActionString("23f3af2899d5f13a", "setMode", params, (success) -> {

            Log.v("execute", success.toString());

        }, this::handleError);
    }

    private void handleError(String message, int code){
        String text = getResources().getString(R.string.error_message, message, code);
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }
}