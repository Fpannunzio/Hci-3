package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Routine {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Routine(String id, String name, Meta meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
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

    public Boolean isFav() {
        return meta.fav;
    }

    public String getDesc() {
        return meta.desc;
    }

    public void setDesc(String desc) {
        this.meta.desc = desc;
    }

    @NonNull
    @Override
    public String toString() {
        return "Routine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", meta=" + meta +
                '}';
    }
}
