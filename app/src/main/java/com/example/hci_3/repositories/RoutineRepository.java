package com.example.hci_3.repositories;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Routine;

import java.util.List;

public class RoutineRepository {

    private static final int pollingDelay = 60000;

    private static RoutineRepository instance;
    private ApiClient apiClient;
    private MutableLiveData<List<Routine>> routines;
    private Handler handler;

    public static synchronized RoutineRepository getInstance() {
        if (instance == null) {
            instance = new RoutineRepository();
        }
        return instance;
    }

    public RoutineRepository() {
        this.apiClient = ApiClient.getInstance();
        routines = new MutableLiveData<>();
        handler = new Handler();
    }

    public MutableLiveData<List<Routine>> getRoutines(){
        return routines;
    }

    public void startPolling(){
        updateRoutines();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, pollingDelay);
                updateRoutines();
            }}, pollingDelay);
    }

    public void stopPolling(){
        handler.removeCallbacksAndMessages(null);
    }

    public void executeRoutine(String routineId, Runnable responseHandler, ApiClient.ErrorHandler errorHandler){
        apiClient.executeRoutine(routineId, responseHandler, errorHandler);
    }

    private void updateRoutines(){
        apiClient.getRoutines(
                this::updateRoutineList,
                (m, c) -> Log.w("uncriticalError", "Failed to get routines: " + m + " Code: " + c)
        );
    }

    private void updateRoutineList(List<Routine> homes){
        this.routines.postValue(homes);
    }
}
