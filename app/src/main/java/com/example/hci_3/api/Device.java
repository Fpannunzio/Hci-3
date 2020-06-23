package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.example.hci_3.api.DeviceStates.DeviceState;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Device {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private DeviceType deviceType;

    @SerializedName("state")
    @Expose
    private DeviceState state;

    @SerializedName("room")
    @Expose
    private Room room;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Device(String id, String name, DeviceType deviceType, DeviceState state, Room room, Meta meta) {
        this.id = id;
        this.name = name;
        this.deviceType = deviceType;
        this.state = state;
        this.room = room;
        this.meta = meta;
    }

    public Device(String id, String name, DeviceType deviceType, DeviceState state, Meta meta) {
        this.id = id;
        this.name = name;
        this.deviceType = deviceType;
        this.state = state;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParsedName(){
        String[] aux = this.name.split("_");
        return aux[aux.length - 1];
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public String getTypeName() {
        return deviceType.getName();
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Meta getMeta() {
        return meta;
    }

//    public void fav() {
//        meta.fav = true;
//    }

//    public void unfav() {
//        meta.fav = false;
//    }

    public Boolean isFav() {
        return meta.fav;
    }

    @NonNull
    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", deviceType=" + deviceType +
                ", state=" + state +
                ", room=" + room +
                ", meta=" + meta +
                '}';
    }

    public Map<String, String> compareToNewerVersion(Device device){
        if(! getId().equals(device.getId()))
            return null;

        Map<String,String> ans = new HashMap<>();

        if(! this.getParsedName().equals(device.getParsedName()))
            ans.put("name",device.getParsedName());

        for(Map.Entry<String,String> entry : getMeta().compareToNewerVersion(device.getMeta()).entrySet()) {
            ans.put("meta." + entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String,String> entry : getRoom().compareToNewerVersion(device.getRoom()).entrySet()) {
            ans.put("room." + entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String,String> entry : getState().compareToNewerVersion(device.getState()).entrySet()) {
            ans.put("state." + entry.getKey(), entry.getValue());
        }

        return ans;
    }
}
