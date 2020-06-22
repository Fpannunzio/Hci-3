package com.example.hci_3.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.hci_3.api.Device;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteViewModel extends DeviceViewModel {

    @Override
    protected LiveData<List<MutableLiveData<Device>>> assignDevicesTransformation() {
        return Transformations.map(deviceRepository.getDevices(),
                devs -> devs.stream()
                        .filter(ldDev -> ldDev.getValue() != null)
                        .filter(ldDev -> ldDev.getValue().isFav())
                        .collect(Collectors.toList()));
    }
}
