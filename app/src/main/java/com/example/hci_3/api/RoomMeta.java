package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomMeta {

    @SerializedName("size")
    @Expose
    private String size;

    public RoomMeta(String size) {
        this.size = size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
        return this.size;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getSize();
    }
}
