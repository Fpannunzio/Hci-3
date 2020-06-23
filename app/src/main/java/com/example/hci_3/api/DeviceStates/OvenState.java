package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class OvenState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("temperature")
    @Expose
    private Integer temperature;

    @SerializedName("heat")
    @Expose
    private String heat;

    @SerializedName("grill")
    @Expose
    private String grill;

    @SerializedName("convection")
    @Expose
    private String convection;

    public OvenState(String status, Integer temperature, String heat, String grill, String convection) {
        this.status = status;
        this.temperature = temperature;
        this.heat = heat;
        this.grill = grill;
        this.convection = convection;
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

    public String getHeat() {
        return heat;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }

    public String getGrill() {
        return grill;
    }

    public void setGrill(String grill) {
        this.grill = grill;
    }

    public String getConvection() {
        return convection;
    }

    public void setConvection(String convection) {
        this.convection = convection;
    }

    @NonNull
    @Override
    public String toString() {
        return "OvenState{" +
                "status='" + status + '\'' +
                ", temperature=" + temperature +
                ", heat='" + heat + '\'' +
                ", grill='" + grill + '\'' +
                ", convection='" + convection + '\'' +
                '}';
    }

    @Override
    public Map<String, String> compareToNewerVersion(DeviceState state) {
        Map<String, String> ans = new HashMap<>();

        if(! (state instanceof OvenState))
            return ans; //TODO: null or empty map

        OvenState oState = (OvenState) state;

        if( ! getStatus().equals(oState.getStatus()))
            ans.put("status",oState.getStatus());

        if( ! getConvection().equals(oState.getConvection()))
            ans.put("convection",oState.getConvection());

        if( ! getGrill().equals(oState.getGrill()))
            ans.put("grill",oState.getGrill());

        if( ! getHeat().equals(oState.getHeat()))
            ans.put("heat",oState.getHeat());

        if( ! getTemperature().equals(oState.getTemperature()))
            ans.put("temperature",oState.getTemperature().toString());

        return ans;
    }
}
