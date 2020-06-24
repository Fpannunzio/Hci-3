package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.example.hci_3.device_views.ACView;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class ACState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("temperature")
    @Expose
    private Integer temperature;

    @SerializedName("mode")
    @Expose
    private String mode;

    @SerializedName("verticalSwing")
    @Expose
    private String verticalSwing;

    @SerializedName("horizontalSwing")
    @Expose
    private String horizontalSwing;

    @SerializedName("fanSpeed")
    @Expose
    private String fanSpeed;

    public ACState(String status, Integer temperature, String mode, String verticalSwing, String horizontalSwing, String fanSpeed) {
        this.status = status;
        this.temperature = temperature;
        this.mode = mode;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getVerticalSwing() {
        return verticalSwing;
    }

    public void setVerticalSwing(String verticalSwing) {
        this.verticalSwing = verticalSwing;
    }

    public String getHorizontalSwing() {
        return horizontalSwing;
    }

    public void setHorizontalSwing(String horizontalSwing) {
        this.horizontalSwing = horizontalSwing;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    @NonNull
    @Override
    public String toString() {
        return "ACState{" +
                "status='" + status + '\'' +
                ", temperature=" + temperature +
                ", mode='" + mode + '\'' +
                ", verticalSwing='" + verticalSwing + '\'' +
                ", horizontalSwing='" + horizontalSwing + '\'' +
                ", fanSpeed='" + fanSpeed + '\'' +
                '}';
    }

    @Override
    public Map<String, String> compareToNewerVersion(DeviceState state) {
        Map<String, String> ans = new HashMap<>();

        if(!(state instanceof ACState))
            return ans;

        ACState acState = (ACState) state;

        if(!getStatus().equals(acState.getStatus()))
            ans.put("status", acState.getStatus());

        if(!getTemperature().equals(acState.getTemperature()))
            ans.put("temperature", acState.getTemperature().toString());

        if(!getFanSpeed().equals(acState.getFanSpeed()))
            ans.put("fanSpeed", acState.getFanSpeed());

        if(!getHorizontalSwing().equals(acState.getHorizontalSwing()))
            ans.put("horizontalSwing", acState.getHorizontalSwing());

        if(!getVerticalSwing().equals(acState.getVerticalSwing()))
            ans.put("verticalSwing", acState.getVerticalSwing());

        if(!getMode().equals(acState.getMode()))
            ans.put("mode", acState.getMode());

        return ans;
    }
}
