package com.example.hci_3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FavoriteDeviceViewModel {

    private DeviceRepository deviceRepository;
    private MutableLiveData<List<MutableLiveData<Device>>> devices;

    public FavoriteDeviceViewModel(){
        deviceRepository = DeviceRepository.getInstance();

        devices = (MutableLiveData<List<MutableLiveData<Device>>>) Transformations.map(deviceRepository.getLiveData(),
                devs -> devs.stream()
                        .filter(ldDev -> ldDev.getValue().isFav())
                        .collect(Collectors.toList()));
    }

    public LiveData<List<MutableLiveData<Device>>> getDevices(){
        return devices;
    }

    public void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.SuccessHandler<Boolean> responseHandler, ApiClient.ErrorHandler errorHandler){
        deviceRepository.
    }

    public void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.ErrorHandler errorHandler){

    }
}
