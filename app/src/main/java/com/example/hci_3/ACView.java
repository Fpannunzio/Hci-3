package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


import com.example.hci_3.api.ApiClient;
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
        LayoutInflater.from(context).inflate(R.layout.ac_view, this, true);

        // Aca guardo los elementos de mi view
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
    public void setDevice(Device device) {
        super.setDevice(device);

        // Aca se cargan los parametros del device
        mDevName.setText(getParsedName(device.getName()));

        mState.setText(getResources().getString(R.string.ac_state, ((ACState) device.getState()).getStatus().equals("on")? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado) , ((ACState) device.getState()).getTemperature()));
        mTemperature.setText(getResources().getString(R.string.ac_temp, String.valueOf(((ACState) device.getState()).getTemperature())));
        mMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int temp = ((ACState) device.getState()).getTemperature() - 1;
                if(temp >= 18)
                    ApiClient.getInstance().executeActionInteger(device.getId(), "setTemperature", new ArrayList<>(Collections.singletonList(temp)), (success) -> {
                        if (success) {
                            ((ACState) device.getState()).setTemperature(temp);
                            mTemperature.setText(getResources().getString(R.string.ac_temp, String.valueOf(temp)));
                            mState.setText(getResources().getString(R.string.ac_state, ((ACState) device.getState()).getStatus().equals("on")? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado) , ((ACState) device.getState()).getTemperature()));
                        }
                }, (m, c) -> handleError(m, c));
                //else
                    //enviar mensaje de error
            }
        });
        mPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int temp = ((ACState) device.getState()).getTemperature() + 1;
                if(temp <= 38)
                    ApiClient.getInstance().executeActionInteger(device.getId(), "setTemperature", new ArrayList<>(Collections.singletonList(temp)), (success) -> {
                        if (success) {
                            ((ACState) device.getState()).setTemperature(temp);
                            mTemperature.setText(getResources().getString(R.string.ac_temp, String.valueOf(temp)));
                            mState.setText(getResources().getString(R.string.ac_state, ((ACState) device.getState()).getStatus().equals("on")? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado) , ((ACState) device.getState()).getTemperature()));
                        }
                    }, (m, c) -> handleError(m, c));
                //else
                //enviar mensaje de error
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    ApiClient.getInstance().executeActionString(device.getId(), "turnOn", new ArrayList<>(), (success) -> {
                        if (success)
                            ((ACState) device.getState()).setStatus("on");
                        mState.setText(getResources().getString(R.string.ac_state, getResources().getString(R.string.prendido) , ((ACState) device.getState()).getTemperature()));
                    }, (m, c) -> handleError(m, c));
                else
                    ApiClient.getInstance().executeActionString(device.getId(), "turnOff", new ArrayList<>(), (success) -> {
                        if (success)
                            ((ACState) device.getState()).setStatus("off");
                        mState.setText(getResources().getString(R.string.ac_state, getResources().getString(R.string.apagado) , ((ACState) device.getState()).getTemperature()));
                    }, (m, c) -> handleError(m, c));
            }
        });

        mTempGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.cold_button)
                        setMode("cold");
                    else if (checkedId == R.id.heat_button)
                        setMode("heat");
                    else
                        setMode("fan");
                }
            }
        });

        mVertGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
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
            }
        });

        mHorGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
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
            }
        });

        mFanSpeedGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
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
            }
        });
    }
    private void setMode(String value){
        ApiClient.getInstance().executeActionString(device.getId(), "setMode", new ArrayList<>(Collections.singletonList(value)), (success) -> {
                    ((ACState) device.getState()).setMode(value);
                },
                this::handleError);
    }

    private void setVerticalSwing(String value){
        ApiClient.getInstance().executeActionString(device.getId(), "setVerticalSwing", new ArrayList<>(Collections.singletonList(value)), (success) -> {
                    ((ACState) device.getState()).setVerticalSwing(value);
                },
                this::handleError);
    }

    private void setHorizontalSwing(String value){
        ApiClient.getInstance().executeActionString(device.getId(), "setHorizontalSwing", new ArrayList<>(Collections.singletonList(value)), (success) -> {
                    ((ACState) device.getState()).setHorizontalSwing(value);
                },
                this::handleError);
    }

    private void setFanSpeed(String value){
        ApiClient.getInstance().executeActionString(device.getId(), "setFanSpeed", new ArrayList<>(Collections.singletonList(value)), (success) -> {
                    ((ACState) device.getState()).setFanSpeed(value);
                },
                this::handleError);
    }
}

