package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaucetState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("dispensedQuantity")
    @Expose
    private Double dispensedQuantity;

    public FaucetState(String status, Integer quantity, String unit, Double dispensedQuantity) {
        this.status = status;
        this.quantity = quantity;
        this.unit = unit;
        this.dispensedQuantity = dispensedQuantity;
    }

    public FaucetState(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getDispensedQuantity() {
        return dispensedQuantity;
    }

    public void setDispensedQuantity(Double dispensedQuantity) {
        this.dispensedQuantity = dispensedQuantity;
    }

    @NonNull
    @Override
    public String toString() {
        return "FaucetState{" +
                "status='" + status + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", dispensedQuantity=" + dispensedQuantity +
                '}';
    }
}
