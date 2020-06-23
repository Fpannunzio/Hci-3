package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

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

    public Boolean getFav() {
        return fav;
    }

    public String getDesc() {
        return desc;
    }

    @NonNull
    @Override
    public String toString() {
        return "Meta{" +
                "fav=" + fav +
                ", desc=" + desc +
                '}';
    }

    public Map<String, String> compareToNewerVersion(Meta meta){
        Map<String, String> ans = new HashMap<>();

        if(getFav() != null && ! getFav().equals(meta.getFav()))
            ans.put("fav", meta.getFav().toString());

        if(getDesc() != null && ! getDesc().equals(meta.getDesc()))
            ans.put("desc", meta.getDesc());

        return ans;
    }
}
