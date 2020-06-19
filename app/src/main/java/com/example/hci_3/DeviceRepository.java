package com.example.hci_3;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceTypeInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeviceRepository {

    private static DeviceRepository instance;
    private ApiClient apiClient;
    private MutableLiveData<List<MutableLiveData<Device>>> devices;
    private Map<String, MutableLiveData<Device>> idToDeviceMap;

    private DeviceRepository(){
        apiClient = ApiClient.getInstance();
        devices = new MutableLiveData<>();
        idToDeviceMap = new HashMap<>();
    }

    public static synchronized DeviceRepository getInstance() {
        if (instance == null) {
            instance = new DeviceRepository();
        }
        return instance;
    }

    public MutableLiveData<List<MutableLiveData<Device>>> getLiveData(){
        return devices;
    }

    public void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.SuccessHandler<Boolean> responseHandler, ApiClient.ErrorHandler errorHandler){
        apiClient.executeAction(deviceId, actionName, params, success -> {
            Device device = idToDeviceMap.get(deviceId).getValue();
            apiClient.getDeviceState(deviceId, device.getState().getClass(), )

            responseHandler.handle(success);
        }, errorHandler);
    }

    private void getDevices(){
        apiClient.getDevices(
                this::updateDeviceList,
                (m, c) -> Log.w("warningcito", "Failed to get devices: " + m + " Code: " + c)
        );
    }

    private void updateDeviceList(List<Device> devs){
        Map<String, MutableLiveData<Device>> auxMap = new HashMap<>();

        List<MutableLiveData<Device>> ans = devs.stream().map(dev -> {
            MutableLiveData<Device> liveData = null;

            if(idToDeviceMap.containsKey(dev.getId()))
                liveData = idToDeviceMap.get(dev.getId());

            if(liveData == null)
                liveData = new MutableLiveData<>();

            auxMap.put(dev.getId(), liveData);
            liveData.postValue(dev);

            return liveData;
        }).collect(Collectors.toList());

        idToDeviceMap = auxMap;

        devices.postValue(ans);
    }
}
