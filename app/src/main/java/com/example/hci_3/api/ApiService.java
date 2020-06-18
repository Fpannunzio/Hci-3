package com.example.hci_3.api;

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

//    @POST("rooms")
//    @Headers("Content-Type: application/json")
//    Call<Result<Room>> addRoom(@Body Room room);
//
//    @PUT("rooms/{roomId}")
//    @Headers("Content-Type: application/json")
//    Call<Result<Boolean>> modifyRoom(@Path("roomId") String roomId, @Body Room room);
//
//    @DELETE("rooms/{roomId}")
//    Call<Result<Boolean>> deleteRoom(@Path("roomId") String roomId);

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

    // Logs

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Object>> executeActionInteger(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body List<Integer> params);

    @PUT("devices/{deviceId}/{actionName}")
    @Headers("Content-Type: application/json")
    Call<Result<Object>> executeActionString(@Path("deviceId") String deviceId, @Path("actionName") String actionName, @Body List<String> params);


}
