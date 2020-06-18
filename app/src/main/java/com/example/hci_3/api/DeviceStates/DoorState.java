package com.example.hci_3.api.DeviceStates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @Override
    public String toString() {
        return "DoorState{" +
                "status='" + status + '\'' +
                ", lock='" + lock + '\'' +
                '}';
    }
}
