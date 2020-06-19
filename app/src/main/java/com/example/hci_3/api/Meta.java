package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("fav")
    @Expose
    public Boolean fav;

    @SerializedName("desc")
    @Expose
    public String desc;

    public Meta(){}

    public Meta(Boolean fav) {
        this.fav = fav;
    }

    public Meta(Boolean fav, String desc) {
        this.fav = fav;
        this.desc = desc;
    }

    @NonNull
    @Override
    public String toString() {
        return "Meta{" +
                "fav=" + fav +
                ", desc=" + desc +
                '}';
    }
}
