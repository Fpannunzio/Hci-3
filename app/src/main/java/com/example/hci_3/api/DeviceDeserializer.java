package com.example.hci_3.api;

import com.example.hci_3.api.DeviceStates.DeviceState;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class DeviceDeserializer implements JsonDeserializer<Device> {

    @Override
    public Device deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        final JsonObject jsonObject = json.getAsJsonObject();

        String id = jsonObject.get("id").getAsString();

        String name = jsonObject.get("name").getAsString();

        final JsonElement jsonElementType = jsonObject.get("type");
        DeviceType deviceType = gson.fromJson(jsonElementType, DeviceType.class);

        DeviceTypeInfo e = DeviceTypeInfo.getFromName(deviceType.getName());
        if(e == null || e.getStateClass() == null)
            throw new RuntimeException("Device type is not supported");

        final JsonElement jsonElementState = jsonObject.get("state");
        DeviceState deviceState = gson.fromJson(jsonElementState, (Type) e.getStateClass());

        final JsonElement jsonElementRoom = jsonObject.get("room");
        Room room = gson.fromJson(jsonElementRoom, Room.class);

        final JsonElement jsonElementMeta = jsonObject.get("meta");
        Meta meta = gson.fromJson(jsonElementMeta, Meta.class);

        return new Device(id, name, deviceType, deviceState, room, meta);
    }
}