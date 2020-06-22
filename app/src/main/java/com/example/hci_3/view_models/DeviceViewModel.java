package com.example.hci_3.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;
import com.example.hci_3.repositories.DeviceRepository;

import java.util.List;

public abstract class DeviceViewModel extends ViewModel {

    protected static DeviceRepository deviceRepository = DeviceRepository.getInstance();

    protected abstract LiveData<List<MutableLiveData<Device>>> assignDevicesTransformation();

    public static void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.ActionResponseHandler responseHandler, ApiClient.ErrorHandler errorHandler){
        deviceRepository.executeAction(deviceId, actionName, params, responseHandler, errorHandler);
    }

    public static void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.ErrorHandler errorHandler){
        deviceRepository.executeAction(deviceId, actionName, params, errorHandler);
    }

    public LiveData<List<MutableLiveData<Device>>> getDevices(){
        return assignDevicesTransformation();
    }
}
