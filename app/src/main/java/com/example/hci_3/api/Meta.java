package com.example.hci_3.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("fav")
    @Expose
    public Boolean fav;

    public Meta(){}

    public Meta(Boolean fav) {
        this.fav = fav;
    }
}
