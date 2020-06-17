package com.example.hci_3.api;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private Retrofit retrofit;
    private ApiService service;
    private static ApiClient instance;
    // Use IP 10.0.2.2 instead of 127.0.0.1 when running Android emulator in the
    // same computer that runs the API.
    private final String BaseURL = "http://10.0.2.2:8080/api/";

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.service = retrofit.create(ApiService.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public Error getError(ResponseBody response) {
        Converter<ResponseBody, ErrorResult> errorConverter =
                this.retrofit.responseBodyConverter(ErrorResult.class, new Annotation[0]);
        try {
            ErrorResult responseError = errorConverter.convert(response);
            assert responseError != null;
            return responseError.getError();
        } catch (IOException e) {
            return null;
        }
    }

    public Call<Result<Room>> getRoom(String roomId, Callback<Result<Room>> callback) {
        Call<Result<Room>> call = this.service.getRoom(roomId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Room>>> getRooms(Callback<Result<List<Room>>> callback) {
        Call<Result<List<Room>>> call = this.service.getRooms();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Device>>> getDevices(Callback<Result<List<Device>>> callback){
        Call<Result<List<Device>>> call = service.getDevices();
        call.enqueue(callback);
        return call;
    }

    public Call<Result<Device>> getDevice(String deviceId, Callback<Result<Device>> callback){
        Call<Result<Device>> call = service.getDevice(deviceId);
        call.enqueue(callback);
        return call;
    }

    public Call<Result<List<Device>>> getDevicesByType(String deviceTypeId, Callback<Result<List<Device>>> callback){
        Call<Result<List<Device>>> call = service.getDevicesByType(deviceTypeId);
        call.enqueue(callback);
        return call;
    }
}
