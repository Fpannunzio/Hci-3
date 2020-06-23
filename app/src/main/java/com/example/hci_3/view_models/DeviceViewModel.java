package com.example.hci_3.view_models;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.DeviceState;
import com.example.hci_3.api.Room;
import com.example.hci_3.repositories.DeviceRepository;
import com.example.hci_3.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class DeviceViewModel extends ViewModel {

    protected DeviceRepository deviceRepository = DeviceRepository.getInstance();
    protected RoomRepository roomRepository = RoomRepository.getInstance();
    protected List<StatePollingHandler> statePollingHandlers = new ArrayList<>();

    protected abstract LiveData<List<MutableLiveData<Device>>> assignDevicesTransformation();

    public void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.ActionResponseHandler responseHandler, ApiClient.ErrorHandler errorHandler){
        deviceRepository.executeAction(deviceId, actionName, params, responseHandler, errorHandler);
    }

    public void executeAction(String deviceId, String actionName, List<Object> params, ApiClient.ErrorHandler errorHandler){
        deviceRepository.executeAction(deviceId, actionName, params, errorHandler);
    }

    public LiveData<List<MutableLiveData<Device>>> getDevices(){
        return assignDevicesTransformation();
    }

    public void getDeviceRooms(Device device, ApiClient.SuccessHandler<List<Room>> responseHandler, ApiClient.ErrorHandler errorHandler){
        roomRepository.getRooms(device.getId(), responseHandler, errorHandler);
    }

    public LiveData<DeviceState> addPollingState(Device device, int interval){
        StatePollingHandler statePollingInfo = new StatePollingHandler(device, interval);
        LiveData<DeviceState> state = statePollingInfo.getState();
        statePollingHandlers.add(statePollingInfo);
        statePollingInfo.startPolling();
        return state;
    }

    public void continuePollingStates(){
        statePollingHandlers.forEach(StatePollingHandler::startPolling);
    }

    public void pausePollingStates(){
        statePollingHandlers.forEach(StatePollingHandler::stopPolling);
    }

    public void stopPollingStates(){
        pausePollingStates();
        statePollingHandlers.clear();
    }



    private class StatePollingHandler {
        String deviceId;
        Class<? extends DeviceState> stateClass;
        Handler handler;
        MutableLiveData<DeviceState> state;
        int delay;
        Boolean isPolling;

        public StatePollingHandler(Device device, int delay) {
            this.deviceId = device.getId();
            this.stateClass = device.getState().getClass();
            this.state = new MutableLiveData<>();
            this.handler = new Handler();
            this.delay = delay;
            this.isPolling = false;
        }

        public LiveData<DeviceState> getState(){
            return state;
        }

        public void startPolling(){
            if(!isPolling) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        deviceRepository.getDeviceState(deviceId, stateClass,
                                state -> updateState(state),
                                (m, c) -> Log.w("uncriticalError", "Failed to get deviceState: " + m + " Code: " + c));
                        handler.postDelayed(this, delay);
                    }
                }, delay);
                isPolling = true;
            }
        }

        public void stopPolling(){
            if(isPolling) {
                handler.removeCallbacksAndMessages(null);
                isPolling = false;
            }
        }

        private void updateState(DeviceState state){
            this.state.postValue(state);
        }
    }
}
