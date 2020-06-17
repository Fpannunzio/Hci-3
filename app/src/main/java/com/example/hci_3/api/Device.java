package com.example.hci_3.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type")
    @Expose
    private Type type;

    @SerializedName("state")
    @Expose
    private State state;

    @SerializedName("room")
    @Expose
    private Room room;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Device(String id, String name, Type type, State state, Room room, Meta meta) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
        this.room = room;
        this.meta = meta;
    }

    public Device(String id, String name, Type type, State state, Meta meta) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.state = state;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getTypeName() {
        return type.getName();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void fav() {
        meta.fav = true;
    }

    public void unfav() {
        meta.fav = false;
    }

    public Boolean isFav() {
        return meta.fav;
    }
}
