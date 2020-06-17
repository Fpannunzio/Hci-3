package com.example.hci_3.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
}
