package com.example.hci_3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;

import java.util.List;

public abstract class DeviceViewModel extends ViewModel {

    protected static DeviceRepository deviceRepository = DeviceRepository.getInstance();
    protected MutableLiveData<List<MutableLiveData<Device>>> devices;

    public DeviceViewModel(){
        assignDevicesTransformation();
    }

    protected abstract void assignDevicesTransformation();

    public static void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.SuccessHandler<Boolean> responseHandler, ApiClient.ErrorHandler errorHandler){
        deviceRepository.executeAction(deviceId, actionName, params, responseHandler, errorHandler);
    }

    public static void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.ErrorHandler errorHandler){
        deviceRepository.executeAction(deviceId, actionName, params, errorHandler);
    }

    public LiveData<List<MutableLiveData<Device>>> getDevices(){
        return devices;
    }
}
