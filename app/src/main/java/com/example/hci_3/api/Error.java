package com.example.hci_3.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error {

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("description")
    @Expose
    private Object description;

    public Error() {
    }

    public Error(int code, Object description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @SuppressWarnings("unchecked") // description is either a String or List<String>. Defined by Api.
    public String getDescription() {
        if(description instanceof List<?>)
            return ((List<String>) description).get(0);
        else
            return (String) description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

}