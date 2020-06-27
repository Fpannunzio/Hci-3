package com.example.hci_3.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class LogEntry {

    @SerializedName("timestamp")
    @Expose
    private Date timestamp;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("params")
    @Expose
    private List<Object> params;

    @SerializedName("result")
    @Expose
    private Object result;

    public LogEntry(Date timestamp, String deviceId, String action, List<Object> params, Object result) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.action = action;
        this.params = params;
        this.result = result;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Boolean wasSuccessful(){
        if(result instanceof Boolean)
            return (Boolean)result;
        else
            return result != null;
    }

    @NonNull
    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", action='" + action + '\'' +
                '}';
    }
}
