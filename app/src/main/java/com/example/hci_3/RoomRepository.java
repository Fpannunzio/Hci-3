package com.example.hci_3;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Room;

import java.util.List;

public class RoomRepository {

    private static RoomRepository instance;
    private ApiClient apiClient;
    private MutableLiveData<List<Room>> rooms;
    private Handler handler;
    private String lastHomeQueriedId;

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

    public MutableLiveData<List<Room>> getRooms(){
        return rooms;
    }

    public void setHomeToQuery(String homeId){
        stopPolling();
        lastHomeQueriedId = homeId;
        startPolling();
        updateRooms(homeId);
    }

    public void stopPolling(){
        handler.removeCallbacksAndMessages(null);
    }

    private void startPolling(){
        // Para mi tiene que suscribirse al alarmManager
        int delay = 60000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, delay);
                updateRooms(lastHomeQueriedId);
            }}, delay);
    }

    private void updateRooms(String homeId){
        apiClient.getHomeRooms(homeId,
                this::updateRoomList,
                (m, c) -> Log.w("uncriticalError", "Failed to get homes: " + m + " Code: " + c)
        );
    }

    private void updateRoomList(List<Room> rooms){
        this.rooms.postValue(rooms);
    }
}
