package com.example.hci_3.api.DeviceStates;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlindsState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("level")
    @Expose
    private Integer level;

    @SerializedName("currentLevel")
    @Expose
    private Integer currentLevel;

    public BlindsState(String status, Integer level, Integer currentLevel) {
        this.status = status;
        this.level = level;
        this.currentLevel = currentLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    @Override
    public String toString() {
        return "BlindsState{" +
                "status='" + status + '\'' +
                ", level=" + level +
                ", currentLevel=" + currentLevel +
                '}';
    }
}