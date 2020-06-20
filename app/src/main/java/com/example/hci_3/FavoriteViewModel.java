package com.example.hci_3;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.hci_3.api.Device;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteViewModel extends DeviceViewModel {

    public FavoriteViewModel(){
        super();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void assignDevicesTransformation() {
        devices = (MutableLiveData<List<MutableLiveData<Device>>>) Transformations.map(deviceRepository.getDevices(),
                devs -> devs.stream()
                        .filter(ldDev -> ldDev.getValue().isFav())
                        .collect(Collectors.toList()));
    }
}
