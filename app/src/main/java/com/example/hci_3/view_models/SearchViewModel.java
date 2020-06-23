package com.example.hci_3.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.hci_3.api.Device;

import java.util.List;
import java.util.stream.Collectors;

public class SearchViewModel extends DeviceViewModel {

    String searchParam;

    public LiveData<List<MutableLiveData<Device>>> setNewSearchParam(String searchParam){
        this.searchParam = searchParam;
        return getDevices();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected LiveData<List<MutableLiveData<Device>>> assignDevicesTransformation() {
        return Transformations.map(deviceRepository.getDevices(),
                devs -> devs.stream()
                        .filter(ldDev -> {
                            Device device = ldDev.getValue();
                            return device.getName().contains(searchParam) ||
                                    device.getRoom().getName().contains(searchParam) ||
                                    device.getRoom().getHome().getName().contains(searchParam);
                        })
                        .collect(Collectors.toList()));
    }
}
