package com.example.hci_3.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.hci_3.api.Device;

import java.util.List;
import java.util.stream.Collectors;

public class RoomDetailsViewModel extends DeviceViewModel{

    String roomId;

    public void setRoom(String roomId){
        this.roomId = roomId;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected LiveData<List<MutableLiveData<Device>>> assignDevicesTransformation() {
        return Transformations.map(deviceRepository.getDevices(),
                devs -> devs.stream()
                        .filter(ldDev -> ldDev.getValue().getRoom().getId().equals(roomId))
                        .collect(Collectors.toList()));
    }
}
