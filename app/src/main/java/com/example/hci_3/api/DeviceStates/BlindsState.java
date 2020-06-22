package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

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

    @NonNull
    @Override
    public String toString() {
        return "BlindsState{" +
                "status='" + status + '\'' +
                ", level=" + level +
                ", currentLevel=" + currentLevel +
                '}';
    }

    @Override
    public Map<String, String> compareToNewerVersion(DeviceState state) {
        Map<String, String> ans = new HashMap<>();

        if(! (state instanceof BlindsState))
            return ans; //TODO: null or empty map

        BlindsState bState = (BlindsState) state;

        if( ! getStatus().equals(bState.getStatus()))
            ans.put("status",bState.getStatus());

        if( ! getLevel().equals(bState.getLevel()))
            ans.put("level",bState.getLevel().toString());

        if( ! getCurrentLevel().equals(bState.getCurrentLevel()))
            ans.put("currentLevel",bState.getCurrentLevel().toString());

        return ans;

    }
}
