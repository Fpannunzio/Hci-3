package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class LampState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("brightness")
    @Expose
    private Integer brightness;

    public LampState(String status, String color, Integer brightness) {
        this.status = status;
        this.color = color;
        this.brightness = brightness;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    @NonNull
    @Override
    public String toString() {
        return "LampState{" +
                "status='" + status + '\'' +
                ", color='" + color + '\'' +
                ", brightness=" + brightness +
                '}';
    }

    @Override
    public Map<String, String> compareToNewerVersion(DeviceState state) {
        Map<String, String> ans = new HashMap<>();

        if(! (state instanceof LampState))
            return ans; //TODO: null or empty map

        LampState lState = (LampState) state;

        if( ! getStatus().equals(lState.getStatus()))
            ans.put("status",lState.getStatus());

        if( ! getColor().equals(lState.getColor()))
            ans.put("color",lState.getColor());

        if( ! getBrightness().equals(lState.getBrightness()))
            ans.put("brightness",lState.getBrightness().toString());

        return ans;
    }
}
