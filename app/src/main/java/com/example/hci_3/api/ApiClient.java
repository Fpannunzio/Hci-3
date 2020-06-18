package com.example.hci_3.api;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hci_3.api.DeviceStates.DeviceState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
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

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(Device.class, new DeviceDeserializer());
        Gson gson = gsonBuilder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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

    public Call<Result<Room>> getRoom(String roomId, SuccessHandler<Room> responseHandler, ErrorHandler errorHandler) {
        Call<Result<Room>> call = this.service.getRoom(roomId);
        call.enqueue(getStandardCallback(responseHandler, errorHandler));
        return call;
    }

    public Call<Result<List<Room>>> getRooms(SuccessHandler<List<Room>> responseHandler, ErrorHandler errorHandler) {
        Call<Result<List<Room>>> call = this.service.getRooms();
        call.enqueue(getStandardCallback(responseHandler, errorHandler));
        return call;
    }

    public Call<Result<List<Device>>> getRoomDevices(String roomId, SuccessHandler<List<Device>> responseHandler, ErrorHandler errorHandler) {
        Call<Result<List<Device>>> call = this.service.getRoomDevices(roomId);
        call.enqueue(getStandardCallback(responseHandler, errorHandler));
        return call;
    }

    public Call<Result<List<Device>>> getDevices(SuccessHandler<List<Device>> responseHandler, ErrorHandler errorHandler){
        Call<Result<List<Device>>> call = service.getDevices();
        call.enqueue(getStandardCallback(responseHandler, errorHandler));
        return call;
    }

    public Call<Result<Device>> getDevice(String deviceId, SuccessHandler<Device> responseHandler, ErrorHandler errorHandler){
        Call<Result<Device>> call = service.getDevice(deviceId);
        call.enqueue(getStandardCallback(responseHandler, errorHandler));
        return call;
    }

    public Call<Result<List<Device>>> getDevicesByType(String deviceTypeId, SuccessHandler<List<Device>> responseHandler, ErrorHandler errorHandler){
        Call<Result<List<Device>>> call = service.getDevicesByType(deviceTypeId);
        call.enqueue(getStandardCallback(responseHandler, errorHandler));
        return call;
    }

    public <T extends DeviceState> Call<Result<JsonElement>> getDeviceState(String deviceId, Class<T> stateClass, SuccessHandler<T> responseHandler, ErrorHandler errorHandler){
        Call<Result<JsonElement>> call = service.getDeviceState(deviceId);
        call.enqueue(getDeviceStateCallback(stateClass, responseHandler, errorHandler));
        return call;
    }

    public Call<Result<Object>> executeActionInteger(String deviceId, String actionName, List<Integer> params, SuccessHandler<Boolean> responseHandler, ErrorHandler errorHandler){
        Call<Result<Object>> call = service.executeActionInteger(deviceId, actionName, params);
        call.enqueue(getExecuteActionCallback(responseHandler, errorHandler));
        return call;
    }

    public Call<Result<Object>> executeActionString(String deviceId, String actionName, List<String> params, SuccessHandler<Boolean> responseHandler, ErrorHandler errorHandler){
        Call<Result<Object>> call = service.executeActionString(deviceId, actionName, params);
        call.enqueue(getExecuteActionCallback(responseHandler, errorHandler));
        return call;
    }

    private <T> Callback<Result<T>> getStandardCallback(SuccessHandler<T> responseHandler, ErrorHandler errorHandler){
        return new Callback<Result<T>>(){

            @Override
            public void onResponse(@NonNull Call<Result<T>> call, @NonNull Response<Result<T>> response){

                if (response.isSuccessful())
                    responseHandler.handle(getResponseData(response));
                else
                    handleError(response, errorHandler);
            }

            @Override
            public void onFailure(@NonNull Call<Result<T>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        };
    }

    private Callback<Result<Object>> getExecuteActionCallback(SuccessHandler<Boolean> responseHandler, ErrorHandler errorHandler){
        return new Callback<Result<Object>>(){
            @Override
            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {

                if (response.isSuccessful()) {
                    Object result = getResponseData(response);

                    if (result instanceof Boolean) {
                        responseHandler.handle((Boolean) result);
                    }else
                        responseHandler.handle(result != null);
                } else
                    handleError(response, errorHandler);
            }

            @Override
            public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        };
    }

    private <T extends DeviceState> Callback<Result<JsonElement>> getDeviceStateCallback(Class<T> stateClass, SuccessHandler<T> responseHandler, ErrorHandler errorHandler){
        return new Callback<Result<JsonElement>>(){

            @Override
            public void onResponse(@NonNull Call<Result<JsonElement>> call, @NonNull Response<Result<JsonElement>> response){

                if (response.isSuccessful()) {
                    JsonElement json = getResponseData(response);
                    if(json == null)
                        throw new RuntimeException("State data was not provided by API");

                    T state = new Gson().fromJson(json.getAsJsonObject().toString(), stateClass);
                    responseHandler.handle(state);
                } else
                    handleError(response, errorHandler);
            }

            @Override
            public void onFailure(@NonNull Call<Result<JsonElement>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        };
    }

    private <T> T getResponseData(@NonNull Response<Result<T>> response){
        Result<T> result = response.body();
        return (result != null)? result.getResult() : null;
    }

    private <T> void handleError(Response<T> response, ErrorHandler handler) {
        Error error = getError(response.errorBody());
        handler.handle(error.getDescription().get(0), error.getCode());
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "com.example.hci_3";
        Log.e(LOG_TAG, t.toString());
    }

    public interface SuccessHandler<T> {
        void handle(T responseData);
    }

    public interface ErrorHandler {
        void handle(String message, int code);
    }
}
