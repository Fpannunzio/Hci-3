package com.example.hci_3.api;

import android.content.Context;

import com.example.hci_3.ACView;
import com.example.hci_3.DeviceView;
import com.example.hci_3.DoorView;
import com.example.hci_3.OvenView;
import com.example.hci_3.VacuumView;
import com.example.hci_3.api.DeviceStates.*;

public enum  DeviceTypeInfo {

    AC("li6cbv5sdlatti0j", "ac"){
        @Override
        public DeviceView getView(Context context) { return new ACView(context); }

        @Override
        public Class<?> getStateClass() { return ACState.class; }
    },

    BLINDS("eu0v2xgprrhhg41g", "blinds") {
        @Override
        public DeviceView getView(Context context) { return null; }

        @Override
        public Class<?> getStateClass() { return BlindsState.class; }
    },

    FAUCET("dbrlsh7o5sn8ur4i", "faucet") {
        @Override
        public DeviceView getView(Context context) { return null; }

        @Override
        public Class<?> getStateClass() { return FaucetState.class; }
    },

    OVEN("im77xxyulpegfmv8", "oven") {
        @Override
        public DeviceView getView(Context context) { return new OvenView(context); }

        @Override
        public Class<?> getStateClass() { return OvenState.class; }
    },

    VACUUM("ofglvd9gqx8yfl3l", "vacuum") {
        @Override
        public DeviceView getView(Context context) { return new VacuumView(context); }

        @Override
        public Class<?> getStateClass() { return VacuumState.class; }
    },

    DOOR("lsf78ly0eqrjbz91", "door") {
        @Override
        public DeviceView getView(Context context) { return new DoorView(context); }

        @Override
        public Class<?> getStateClass() { return DoorState.class; }
    },

    SPEAKER("c89b94e8581855bc", "speaker") {
        @Override
        public DeviceView getView(Context context) { return null; }

        @Override
        public Class<?> getStateClass() { return SpeakerState.class; }
    },

    LAMP("go46xmbqeomjrsjr", "lamp") {
        @Override
        public DeviceView getView(Context context) { return null; }

        @Override
        public Class<?> getStateClass() { return LampState.class; }
    };

    public String id;
    public String name;

    DeviceTypeInfo(String id, String name){
        this.id = id;
        this.name = name;
    }

    public static DeviceTypeInfo getFromName(String name){
        for(DeviceTypeInfo e : values()){
            if(e.name.equals(name))
                return e;
        }
        return null;
    }

    public static DeviceTypeInfo getFromId(String id){
        for(DeviceTypeInfo e : values()){
            if(e.id.equals(id))
                return e;
        }
        return null;
    }

    public abstract DeviceView getView(Context context);

    public abstract Class<?> getStateClass();
}
