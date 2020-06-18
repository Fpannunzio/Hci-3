package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.hci_3.api.Device;

public class ACView extends DeviceView {
    private TextView mDevName;

    public ACView(Context context) {
        super(context);
    }

    public ACView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ACView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.ac_view, this, true);

        // Aca guardo los elementos de mi view
        mDevName = findViewById(R.id.device_name);
    }

    @Override
    public void setDevice(Device device) {
        super.setDevice(device);

        // Aca se cargan los parametros del device
        mDevName.setText(this.device.getName());
    }
}
