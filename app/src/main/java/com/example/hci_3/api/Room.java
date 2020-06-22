package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

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

    public String getParsedName(){
        String[] aux = this.name.split("_");
        return aux[aux.length - 1];
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

    public Map<String, String> compareToNewerVersion(Room room) {
        Map<String, String> ans = new HashMap<>();

        if(! getParsedName().equals(room.getParsedName()))
            ans.put("name", room.getParsedName());

        for(Map.Entry<String,String> entry : getMeta().compareToNewerVersion(room.getMeta()).entrySet()) {
            ans.put("meta." + entry.getKey(), entry.getValue());
        }

        for(Map.Entry<String,String> entry : getHome().compareToNewerVersion(room.getHome()).entrySet()) {
            ans.put("home." + entry.getKey(), entry.getValue());
        }
        return ans;
    }
}
