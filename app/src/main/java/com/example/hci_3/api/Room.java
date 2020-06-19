package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("home")
    @Expose
    private Home home;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Room(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Room(String id, String name, Home home) {
        this.id = id;
        this.name = name;
        this.home = home;
    }

    public Room(String id, String name, Home home, Meta meta) {
        this.id = id;
        this.name = name;
        this.home = home;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Meta getMeta() {
        return meta;
    }

    @NonNull
    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", home=" + home +
                ", meta=" + meta +
                '}';
    }
}
