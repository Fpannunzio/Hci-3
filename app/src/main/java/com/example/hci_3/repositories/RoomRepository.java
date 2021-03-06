package com.example.hci_3.repositories;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Home;
import com.example.hci_3.api.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository {

    private static final int POLLING_DELAY = 60000;

    private static RoomRepository instance;
    private ApiClient apiClient;
    private MutableLiveData<List<Room>> rooms;
    private Handler handler;
    private Home lastHomeQueried;

    public static synchronized RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    public RoomRepository() {
        this.apiClient = ApiClient.getInstance();
        rooms = new MutableLiveData<>();
        handler = new Handler();
    }

    public void getRooms(String homeId, ApiClient.SuccessHandler<List<Room>> responseHandler, ApiClient.ErrorHandler errorHandler){
        apiClient.getHomeRooms(homeId, responseHandler, errorHandler);
    }

    public MutableLiveData<List<Room>> getRooms(){
        return rooms;
    }

    public void setHomeToQuery(Home home){
        stopPolling();
        lastHomeQueried = home;
        startPolling();
        updateRooms(home);
    }

    public void stopPolling(){
        handler.removeCallbacksAndMessages(null);
    }

    public void startPolling(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, POLLING_DELAY);
                updateRooms(lastHomeQueried);
            }}, POLLING_DELAY);
    }

    private void updateRooms(Home home){
        if(home == null)
            return;

        apiClient.getHomeRooms(home.getId(),
                rooms -> updateRoomList(rooms, home),
                (m, c) -> {
                    Log.w("uncriticalError", "Failed to get homes: " + m + " Code: " + c);
                    updateRoomList(new ArrayList<>(), home);
                }

        );
    }

    private void updateRoomList(List<Room> rooms, Home home){
        rooms.forEach(room -> room.setHome(home));
        this.rooms.postValue(rooms);
    }
}
