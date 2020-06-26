package com.example.hci_3.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceType;
import com.example.hci_3.api.Home;
import com.example.hci_3.api.Room;
import com.example.hci_3.repositories.DeviceRepository;
import com.example.hci_3.repositories.HomeRepository;
import com.example.hci_3.repositories.RoomRepository;

import java.util.List;
import java.util.stream.Collectors;

public class HomesViewModel extends ViewModel {

    private HomeRepository homeRepository;
    private RoomRepository roomRepository;
    private MutableLiveData<List<Home>> homes;
    private MutableLiveData<List<Room>> rooms;
    private Boolean isPollingHomes;
    private Boolean isPollingRooms;

    public HomesViewModel(){
        homeRepository = HomeRepository.getInstance();
        roomRepository = RoomRepository.getInstance();
        homes = homeRepository.getHomes();
        rooms = roomRepository.getRooms();
        isPollingHomes = false;
        isPollingRooms = false;
    }

    public void startUpdatingHomes(){
        if(!isPollingHomes) {
            homeRepository.startPolling();
            isPollingHomes = true;
        }
    }

    public void startUpdatingRooms(){
        if(!isPollingRooms) {
            roomRepository.startPolling();
            isPollingRooms = false;
        }
    }

    public void stopUpdatingHomes(){
        if(isPollingHomes) {
            homeRepository.stopPolling();
            if(isPollingRooms) {
                roomRepository.stopPolling();
                isPollingRooms = true;
            }
            isPollingHomes = false;
        }
    }

    public void updateCurrentHome(Home home){
        roomRepository.setHomeToQuery(home);
    }

    public LiveData<List<Home>> getHomes(){
        return homes;
    }

    public LiveData<List<Room>> getRooms(){
        return rooms;
    }
}
