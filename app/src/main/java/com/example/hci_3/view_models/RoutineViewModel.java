package com.example.hci_3.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Routine;
import com.example.hci_3.repositories.DeviceRepository;
import com.example.hci_3.repositories.RoutineRepository;

import java.util.List;

public class RoutineViewModel extends ViewModel {

    private Boolean isPolling = false;
    protected RoutineRepository routineRepository = RoutineRepository.getInstance();
    protected DeviceRepository deviceRepository = DeviceRepository.getInstance();

    public LiveData<List<Routine>> getRoutines(){
        return routineRepository.getRoutines();
    }

    public void startPolling(){
        if(!isPolling) {
            routineRepository.startPolling();
            isPolling = true;
        }
    }

    public void stopPolling(){
        if(isPolling) {
            routineRepository.stopPolling();
            isPolling = false;
        }
    }

    public void executeRoutine(Routine routine, Runnable responseHandler, ApiClient.ErrorHandler errorHandler){
        routineRepository.executeRoutine(routine.getId(), () -> {
            deviceRepository.updateDevices();
            responseHandler.run();
        }
        , errorHandler);
    }

    public void executeRoutine(Routine routine, ApiClient.ErrorHandler errorHandler){
        executeRoutine(routine, () -> {}, errorHandler);
    }
}
