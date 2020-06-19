package com.example.hci_3.api;

import com.example.hci_3.api.DeviceStates.DeviceState;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Room Api Calls

    @GET("rooms/{roomId}")
    Call<Result<Room>> getRoom(@Path("roomId") String roomId);

    @GET("rooms")
    Call<Result<List<Room>>> getRooms();

    @GET("rooms/{roomId}/devices")
    Call<Result<List<Device>>> getRoomDevices(@Path("roomId") String roomId);

    // Device Api Calls

    @GET("devices")
    Call<Result<List<Device>>> getDevices();

    @GET("devices/{deviceId}")
    Call<Result<Device>> getDevice(@Path("deviceId") String deviceId);

    @GET("devices/devicetypes/{deviceTypeId}")
    Call<Result<List<Device>>> getDevicesByType(@Path("deviceTypeId") String deviceTypeId);

    @GET("devices/{deviceId}/state")
    Call<Result<JsonElement>> getDeviceState(@Path("deviceId") String deviceId);

    @GET("devices/logs/limit/{limit}/offset/{offset}")
    Call<Result<List<LogEntry>>> getLogs(@Path("limit") String limit, @Path("offset") String offset);

    @GET("devices/{deviceId}/logs/limit/{limit}/offset/{offset}")
    Call<Result<List<LogEntry>>> getDeviceLogs(@Path("deviceId") String deviceId, @Path("limit") String limit, @Path("offset") String offset);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Object>> executeAction(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body List<Object> params);

    // DeviceType Api Calls

    // Home Api Calls

    @GET("homes/{homeId}")
    Call<Result<Home>> getHome(@Path("homeId") String homeId);

    @GET("homes")
    Call<Result<List<Home>>> getHomes();

    @GET("homes/{homeId}/rooms")
    Call<Result<List<Room>>> getHomeRooms(@Path("homeId") String homeId);

    // Routines Api Calls

    @GET("routines/{routineId}")
    Call<Result<Routine>> getRoutine(@Path("routineId") String routineId);

    @GET("routines")
    Call<Result<List<Routine>>> getRoutines();

    @PUT("routines/{routineId}/execute")
    @Headers("Content-Type: application/json")
    Call<Void> executeRoutine(@Path("routineId") String routineId);

}
