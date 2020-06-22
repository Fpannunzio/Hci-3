package com.example.hci_3.repositories;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Home;

import java.util.List;

public class HomeRepository {

    private static HomeRepository instance;
    private ApiClient apiClient;
    private MutableLiveData<List<Home>> homes;
    private Handler handler;

    public static synchronized HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return instance;
    }

    public HomeRepository() {
        this.apiClient = ApiClient.getInstance();
        homes = new MutableLiveData<>();
        handler = new Handler();
    }

    public MutableLiveData<List<Home>> getHomes(){
        return homes;
    }

    public void startPolling(){
        updateHomes();

        int delay = 60000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, delay);
                updateHomes();
            }}, delay);
    }

    public void stopPolling(){
        handler.removeCallbacksAndMessages(null);
    }

    private void updateHomes(){
        apiClient.getHomes(
                this::updateHomeList,
                (m, c) -> Log.w("uncriticalError", "Failed to get homes: " + m + " Code: " + c)
        );
    }

    private void updateHomeList(List<Home> homes){
        this.homes.postValue(homes);
    }
}
