package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Home {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Home(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Home(String id, String name, Meta meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Meta getMeta() {
        return meta;
    }

    @NonNull
    @Override
    public String toString() {
        return "Home{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", meta=" + meta +
                '}';
    }

    public Map<String, String> compareToNewerVersion(Home home) {
        Map<String, String> ans = new HashMap<>();

        if(! getName().equals(home.getName()))
            ans.put("name", home.getName());

        for(Map.Entry<String,String> entry : getMeta().compareToNewerVersion(home.getMeta()).entrySet()) {
            ans.put("meta." + entry.getKey(), entry.getValue());
        }

        return ans;
    }
}
