package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;

import java.util.ArrayList;

public class LampView extends DeviceView {
    private TextView mDevName, mState;

    private Switch mSwitch;

    public LampView(Context context) {
        super(context);
    }

    public LampView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LampView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.lamp_view, this, true);

        // Aca guardo los elementos de mi view
        mDevName = findViewById(R.id.textLampView);
        mState = findViewById(R.id.onStateLamp);
        mSwitch = findViewById(R.id.lamp_switch);
    }

    @Override
    public void setDevice(Device device) {
        super.setDevice(device);

        // Aca se cargan los parametros del device
        mDevName.setText(this.device.getName());
        /*mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked())
                    ApiClient.getInstance().executeActionString(device.getId(), "turnOn", new ArrayList<>(), (success) -> {
                        if (success)
                            mState.setText(getResources().getString(R.string.prendido));
                    }, (m, c) -> handleError(m, c));
                else
                    ApiClient.getInstance().executeActionString(device.getId(), "turnOff", new ArrayList<>(), (success) -> {
                        if (success)
                            mState.setText(getResources().getString(R.string.apagado));
                    }, (m, c) -> handleError(m, c));
            }
        });*/
    }

}
