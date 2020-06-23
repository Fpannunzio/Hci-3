package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class DoorState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("lock")
    @Expose
    private String lock;

    public DoorState(String status, String lock) {
        this.status = status;
        this.lock = lock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    @NonNull
    @Override
    public String toString() {
        return "DoorState{" +
                "status='" + status + '\'' +
                ", lock='" + lock + '\'' +
                '}';
    }

    @Override
    public Map<String, String> compareToNewerVersion(DeviceState state) {
        Map<String, String> ans = new HashMap<>();

        if(!(state instanceof DoorState))
            return ans;

        DoorState dState = (DoorState) state;

        if(!getStatus().equals(dState.getStatus()))
            ans.put("status", dState.getStatus());

        if(!getLock().equals(dState.getLock()))
            ans.put("lock", dState.getLock());

        return ans;
    }
}
