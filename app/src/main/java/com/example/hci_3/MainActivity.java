package com.example.hci_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.Error;
import com.example.hci_3.api.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("hola", "holaaa");

        ApiClient.getInstance().getDevicesByType("li6cbv5sdlatti0j", new Callback<Result<List<Device>>>(){

            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response){
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    List<Device> devices = (result != null)? result.getResult() : null;

                    DeviceAdapter adapter = new DeviceAdapter(devices);
                    rv = findViewById(R.id.recyclerView);
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Device>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "com.example.hci_3";
        Log.e(LOG_TAG, t.toString());
    }
}