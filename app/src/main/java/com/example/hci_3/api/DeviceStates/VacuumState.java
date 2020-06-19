package com.example.hci_3.api.DeviceStates;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VacuumState implements DeviceState {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("mode")
    @Expose
    private String mode;

    @SerializedName("batteryLevel")
    @Expose
    private Integer batteryLevel;

    @SerializedName("location")
    @Expose
    private Location location;

    public VacuumState(String status, String mode, Integer batteryLevel, Location location) {
        this.status = status;
        this.mode = mode;
        this.batteryLevel = batteryLevel;
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return "VacuumState{" +
                "status='" + status + '\'' +
                ", mode='" + mode + '\'' +
                ", batteryLevel=" + batteryLevel +
                ", location=" + location +
                '}';
    }

    public static class Location {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        public Location(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return "Location{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
