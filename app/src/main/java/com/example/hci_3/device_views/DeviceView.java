package com.example.hci_3.device_views;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.hci_3.view_models.DeviceViewModel;
import com.example.hci_3.R;
import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;

import java.util.ArrayList;
import java.util.List;

public abstract class DeviceView extends ConstraintLayout {
    protected LiveData<Device> device;
    protected Context context;

    public DeviceView(Context context) {
        super(context);
        init(context);
    }

    public DeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context){
        this.context = context;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }

    public void setDevice(LiveData<Device> device) {
        this.device = device;
        this.device.observe(getLifecycleOwner(), this::onDeviceRefresh);
    }

    public abstract void onDeviceRefresh(Device device);

    protected String getParsedName(String fullName){
        String[] aux = fullName.split("_");
        // Aca se cargan los parametros del device
        return aux[aux.length - 1];
    }

    @SuppressWarnings("ConstantConditions")
    public void executeAction(String actionName, List<Object> params, ApiClient.SuccessHandler<Boolean> responseHandler, ApiClient.ErrorHandler errorHandler){
        DeviceViewModel.executeAction(device.getValue().getId(), actionName, params, responseHandler, errorHandler);
    }

    @SuppressWarnings("ConstantConditions")
    public void executeAction(String actionName, List<Object> params, ApiClient.ErrorHandler errorHandler){
        DeviceViewModel.executeAction(device.getValue().getId(), actionName, params, errorHandler);
    }

    public void executeAction(String actionName, ApiClient.ErrorHandler errorHandler){
        executeAction(actionName, new ArrayList<>(), errorHandler);
    }

    protected void handleError(String message, int code){
        String text = getResources().getString(R.string.error_message, message, code);
        Toast.makeText(this.context, text, Toast.LENGTH_LONG).show();
    }

    protected LifecycleOwner getLifecycleOwner() {
        Context context = getContext();
        while (!(context instanceof LifecycleOwner)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (LifecycleOwner) context;
    }
}
