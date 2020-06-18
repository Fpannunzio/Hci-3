package com.example.hci_3.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceType {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("powerUsage")
    @Expose
    private Integer powerUsage;

    public DeviceType(String id, String name, Integer powerUsage) {
        this.id = id;
        this.name = name;
        this.powerUsage = powerUsage;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPowerUsage() {
        return powerUsage;
    }
}
