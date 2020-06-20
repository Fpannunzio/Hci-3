package com.example.hci_3;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.hci_3.api.Device;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteDeviceViewModel extends DeviceViewModel {

    public FavoriteDeviceViewModel(){
        super();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void assignDevicesTransformation() {
        devices = (MutableLiveData<List<MutableLiveData<Device>>>) Transformations.map(deviceRepository.getLiveData(),
                devs -> devs.stream()
                        .filter(ldDev -> ldDev.getValue().isFav())
                        .collect(Collectors.toList()));
    }
}
