package com.example.hci_3.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.hci_3.repositories.DeviceRepository;

public class ActivityViewModel extends AndroidViewModel {

    private DeviceRepository deviceRepository = DeviceRepository.getInstance();
    private Boolean isPolling = false;

    public ActivityViewModel(@NonNull Application application) {
        super(application);
        deviceRepository.setApplication(application);
    }

    public void startPollingDevices(){
        if(!isPolling){
            deviceRepository.startPolling();
            isPolling = true;
        }
    }

    public void stopPollingDevices(){
        if(isPolling){
            deviceRepository.stopPolling();
            isPolling = false;
        }
    }
}
