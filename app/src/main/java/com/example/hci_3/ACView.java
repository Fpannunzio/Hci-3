package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;

import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


import androidx.lifecycle.LiveData;

import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.ACState;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

import java.util.Collections;

public class ACView extends DeviceView {
    private TextView mDevName, mState, mTemperature;
    private MaterialButtonToggleGroup mTempGroup, mVertGroup, mHorGroup, mFanSpeedGroup;
    private Switch mSwitch;
    private ImageButton mMinus, mPlus;

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
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.ac_view, this, true);

        mDevName = findViewById(R.id.ac_name);
        mTemperature = findViewById(R.id.ac_temp);
        mSwitch = findViewById(R.id.ac_switch);
        mState = findViewById(R.id.onStateAc);
        mTempGroup = findViewById(R.id.temp_group);
        mVertGroup = findViewById(R.id.vertical_toggle_group);
        mHorGroup = findViewById(R.id.horizontal_toggle_group);
        mFanSpeedGroup = findViewById(R.id.fan_speed_toggle_group);
        mMinus = findViewById(R.id.ac_minus);
        mPlus = findViewById(R.id.ac_plus);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        @SuppressWarnings("ConstantConditions")
        ACState state = (ACState) device.getValue().getState();

        // Aca se cargan los parametros del device
        mDevName.setText(getParsedName(device.getValue().getName()));

        mMinus.setOnClickListener(v -> {
            int temp = state.getTemperature() - 1;

            if(temp >= 18)
                setTemperature(temp);
            //else
                //enviar mensaje de error. Por ejemplo un Toast! (tobi)
        });

        mPlus.setOnClickListener(v -> {
            int temp = state.getTemperature() + 1;

            if(temp <= 38)
                setTemperature(temp);
            //else
                //enviar mensaje de error. Por ejemplo un Toast! (tobi)
        });

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                turnOn();
            else
                turnOff();
        });

        mTempGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.cold_button)
                    setMode("cold");
                else if (checkedId == R.id.heat_button)
                    setMode("heat");
                else
                    setMode("fan");
            }
        });

        mVertGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                switch (checkedId) {
                    case R.id.v_auto_button:
                        setVerticalSwing("auto");
                        break;
                    case R.id.v_22_button:
                        setVerticalSwing("22");
                        break;
                    case R.id.v_45_button:
                        setVerticalSwing("45");
                        break;
                    case R.id.v_67_button:
                        setVerticalSwing("67");
                        break;
                    case R.id.v_90_button:
                        setVerticalSwing("90");
                        break;
                }
            }
        });

        mHorGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                switch (checkedId) {
                    case R.id.h_auto_button:
                        setHorizontalSwing("auto");
                        break;
                    case R.id.h_n90_button:
                        setHorizontalSwing("-90");
                        break;
                    case R.id.h_n45_button:
                        setHorizontalSwing("-45");
                        break;
                    case R.id.h_0_button:
                        setHorizontalSwing("0");
                        break;
                    case R.id.h_45_button:
                        setHorizontalSwing("45");
                        break;
                    case R.id.h_90_button:
                        setHorizontalSwing("90");
                        break;
                }
            }
        });

        mFanSpeedGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                switch (checkedId) {
                    case R.id.fs_auto_button:
                        setFanSpeed("auto");
                        break;
                    case R.id.fs_25_button:
                        setFanSpeed("25");
                        break;
                    case R.id.fs_50_button:
                        setFanSpeed("50");
                        break;
                    case R.id.fs_75_button:
                        setFanSpeed("75");
                        break;
                    case R.id.fs_100_button:
                        setFanSpeed("100");
                        break;
                }
            }
        });
    }

    @Override
    public void onDeviceRefresh(Device device) {
        Log.v("deviceStateChange", "AC");
        ACState state = (ACState) device.getState();

        mState.setText(getResources().getString(R.string.ac_state,
                state.getStatus().equals("on")? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado),
                state.getTemperature()));

        mTemperature.setText(getResources().getString(R.string.ac_temp, String.valueOf(state.getTemperature())));

        // Estaria bueno que se coloreen los buttonGroup segun el estado.
    }

    private void turnOn(){
        executeAction("turnOn", this::handleError);
    }

    private void turnOff(){
        executeAction("turnOff", this::handleError);
    }

    private void setTemperature(int temp){
        executeAction("setTemperature", new ArrayList<>(Collections.singletonList(temp)), this::handleError);
    }

    private void setMode(String value){
        executeAction("setMode", new ArrayList<>(Collections.singletonList(value)), this::handleError);
    }

    private void setVerticalSwing(String value){
        executeAction("setVerticalSwing", new ArrayList<>(Collections.singletonList(value)), this::handleError);
    }

    private void setHorizontalSwing(String value){
        executeAction("setHorizontalSwing", new ArrayList<>(Collections.singletonList(value)), this::handleError);
    }

    private void setFanSpeed(String value){
        executeAction("setFanSpeed", new ArrayList<>(Collections.singletonList(value)), this::handleError);
    }
}

