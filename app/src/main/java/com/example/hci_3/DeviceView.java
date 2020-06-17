package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hci_3.api.Device;

public class DeviceView extends ConstraintLayout { //ACView. Queda como DeviceView para pruebas
    private TextView mDevName;
    private Device device;

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


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.ac_view, this, true);

        // Aca guardo los elementos de mi view
        mDevName = findViewById(R.id.device_name);
    }

    public void setDevice(Device device) {
        this.device = device;

        // Aca se cargan los parametros del device
        mDevName.setText(this.device.getName());
    }
}
