package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hci_3.api.Device;

public abstract class DeviceView extends ConstraintLayout {
    protected Device device;
    protected Context context;

    public DeviceView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public DeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public DeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    protected abstract void init(Context context);

    public void setDevice(Device device) {
        this.device = device;
    }

    protected String getParsedName(String fullName){
        String[] aux = fullName.split("_");
        // Aca se cargan los parametros del device
        return aux[aux.length - 1];
    }
    protected void handleError(String message, int code){
        String text = getResources().getString(R.string.error_message, message, code);
        Toast.makeText(this.context, text, Toast.LENGTH_LONG).show();
    }
}
