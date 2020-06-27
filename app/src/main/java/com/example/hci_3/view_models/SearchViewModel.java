package com.example.hci_3.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.hci_3.api.Device;

import java.util.List;
import java.util.stream.Collectors;

public class SearchViewModel extends DeviceViewModel {

    String searchParam;

    public void setNewSearchParam(String searchParam){
        this.searchParam = searchParam;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected LiveData<List<MutableLiveData<Device>>> assignDevicesTransformation() {
        return Transformations.map(deviceRepository.getDevices(),
                devs -> devs.stream()
                        .filter(ldDev -> {
                            Device device = ldDev.getValue();
                            return device.getParsedName().toLowerCase().contains(searchParam.toLowerCase()) ||
                                    device.getRoom().getParsedName().toLowerCase().contains(searchParam.toLowerCase()) ||
                                    device.getRoom().getHome().getName().toLowerCase().contains(searchParam.toLowerCase());
                        })
                        .collect(Collectors.toList()));
    }
}
