package com.example.hci_3;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceType;
import com.example.hci_3.api.Home;
import com.example.hci_3.api.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomesViewModel extends ViewModel {

    private HomeRepository homeRepository;
    private RoomRepository roomRepository;
    private DeviceRepository deviceRepository;
    private MutableLiveData<List<Home>> homes;
    private MutableLiveData<List<Room>> rooms;
    private LiveData<List<DeviceType>> deviceTypes;
    private Home currentHome;
    private Boolean isPollingHomes;

    public HomesViewModel(){
        homeRepository = HomeRepository.getInstance();
        roomRepository = RoomRepository.getInstance();
        deviceRepository = DeviceRepository.getInstance();
        homes = homeRepository.getHomes();
        rooms = roomRepository.getRooms();
        deviceTypes = Transformations.map(deviceRepository.getDevices(), this::getHomesDeviceTypes);
    }

    public void startUpdatingHomes(){
        if(!isPollingHomes) {
            homeRepository.startPolling();
            isPollingHomes = true;
        }
    }

    public void stopUpdatingHomes(){
        if(isPollingHomes) {
            homeRepository.stopPolling();
            isPollingHomes = false;
        }
    }

    public void updateCurrentHome(Home home){
        currentHome = home;
        roomRepository.setHomeToQuery(home.getId());
    }

    public LiveData<List<Home>> getHomes(){
        return homes;
    }

    public LiveData<List<Room>> getRooms(Home home){
        return rooms;
    }

    @SuppressWarnings("ConstantConditions")
    private List<DeviceType> getHomesDeviceTypes(List<MutableLiveData<Device>> devices){
        return devices.stream()
                .map(LiveData::getValue)
                .filter(d -> currentHome.equals(d.getRoom().getHome()))
                .map(Device::getDeviceType)
                .distinct()
                .collect(Collectors.toList());
    }
}
