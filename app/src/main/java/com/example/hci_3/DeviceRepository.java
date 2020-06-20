package com.example.hci_3;


import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeviceRepository {

    private static DeviceRepository instance;
    private ApiClient apiClient;
    private MutableLiveData<List<MutableLiveData<Device>>> devices;
    private Map<String, MutableLiveData<Device>> idToDeviceMap;
    private Handler handler;

    private DeviceRepository(){
        apiClient = ApiClient.getInstance();
        devices = new MutableLiveData<>();
        idToDeviceMap = new HashMap<>();

        // Para mi tiene que ser un alarmManager
        handler = new Handler();
        int delay = 1000;

        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                handler.postDelayed(this, delay);
                getDevices();
            }
        }, delay);
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

            if(success) {
                MutableLiveData<Device> ldDevice = idToDeviceMap.get(deviceId);
                assert ldDevice != null;
                Device device = ldDevice.getValue();
                assert device != null;
                apiClient.getDeviceState(deviceId, device.getState().getClass(), state -> {
                    device.setState(state);
                    ldDevice.postValue(device);
                    responseHandler.handle(true);
                }, (m, c) -> Log.w("uncriticalError", "Failed to get device state: " + m + " Code: " + c));

            } else
                responseHandler.handle(false);

        }, errorHandler);
    }

    public void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.ErrorHandler errorHandler){
        executeAction(deviceId, actionName, params, bool -> {}, errorHandler);
    }

    private void getDevices(){
        apiClient.getDevices(
                this::updateDeviceList,
                (m, c) -> Log.w("uncriticalError", "Failed to get devices: " + m + " Code: " + c)
        );
    }

    private void updateDeviceList(List<Device> devs){
        devs.forEach(d -> Log.v("checkDevices", d.toString()));
        Map<String, MutableLiveData<Device>> auxMap = new HashMap<>();

        List<MutableLiveData<Device>> ans = devs.stream().map(dev -> {
            Log.v("DevicesAdded", dev.toString());
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

//    public enum DeviceDataState {
//        UNCHANGED, UPDATED, REMOVED, ADDED
//    }
}
