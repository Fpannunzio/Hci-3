package com.example.hci_3.device_views;

import android.content.Context;
import android.util.AttributeSet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.lifecycle.LiveData;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.ACState;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ACView extends DeviceView {
    private TextView mDevName, mState, mTemperature, mLocation;
    private MaterialButtonToggleGroup mTempGroup, mVertGroup, mHorGroup, mFanSpeedGroup;
    private Switch mSwitch;
    private ImageButton mMinus, mPlus;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private Map<String, Integer> actionToIdMap;
    private Map<Integer, String> idToActionMap;

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
        mLocation = findViewById(R.id.ac_location);
        mSwitch = findViewById(R.id.ac_switch);
        mState = findViewById(R.id.onStateAc);
        mTempGroup = findViewById(R.id.temp_group);
        mVertGroup = findViewById(R.id.vertical_toggle_group);
        mHorGroup = findViewById(R.id.horizontal_toggle_group);
        mFanSpeedGroup = findViewById(R.id.fan_speed_toggle_group);
        mMinus = findViewById(R.id.ac_minus);
        mPlus = findViewById(R.id.ac_plus);
        cardView = findViewById(R.id.room_card);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        setUpMaps();
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        @SuppressWarnings("ConstantConditions")
        ACState state = (ACState) device.getValue().getState();

        // Aca se cargan la funcionalidad de los elementos UI

        extendBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.VISIBLE);
                // Falta rotar la flecha
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
            }
        });

        mMinus.setOnClickListener(v -> {
            int temp = getTemperatureValue() - 1;

            if(temp >= 18)
                setTemperature(temp);
            else
                Toast.makeText(context, getResources().getString(R.string.invalid_temp), Toast.LENGTH_LONG).show();
        });

        mPlus.setOnClickListener(v -> {
            int temp = getTemperatureValue() + 1;

            if(temp <= 38)
                setTemperature(temp);
            else
                Toast.makeText(context, getResources().getString(R.string.invalid_temp), Toast.LENGTH_LONG).show();
        });

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                turnOn();
            else
                turnOff();
        });

        mTempGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setMode(idToActionMap.get(checkedId));
//                if (checkedId == R.id.cold_button)
//                    setMode("cold");
//                else if (checkedId == R.id.heat_button)
//                    setMode("heat");
//                else
//                    setMode("fan");
        });

        mVertGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setVerticalSwing(idToActionMap.get(checkedId));
//                switch (checkedId) {
//                    case R.id.v_auto_button:
//                        setVerticalSwing("auto");
//                        break;
//                    case R.id.v_22_button:
//                        setVerticalSwing("22");
//                        break;
//                    case R.id.v_45_button:
//                        setVerticalSwing("45");
//                        break;
//                    case R.id.v_67_button:
//                        setVerticalSwing("67");
//                        break;
//                    case R.id.v_90_button:
//                        setVerticalSwing("90");
//                        break;
//                }
        });

        mHorGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setHorizontalSwing(idToActionMap.get(checkedId));
//                switch (checkedId) {
//                    case R.id.h_auto_button:
//                        setHorizontalSwing("auto");
//                        break;
//                    case R.id.h_n90_button:
//                        setHorizontalSwing("-90");
//                        break;
//                    case R.id.h_n45_button:
//                        setHorizontalSwing("-45");
//                        break;
//                    case R.id.h_0_button:
//                        setHorizontalSwing("0");
//                        break;
//                    case R.id.h_45_button:
//                        setHorizontalSwing("45");
//                        break;
//                    case R.id.h_90_button:
//                        setHorizontalSwing("90");
//                        break;
//                }

        });

        mFanSpeedGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setFanSpeed(idToActionMap.get(checkedId));
//                switch (checkedId) {
//                    case R.id.fs_auto_button:
//                        setFanSpeed("auto");
//                        break;
//                    case R.id.fs_25_button:
//                        setFanSpeed("25");
//                        break;
//                    case R.id.fs_50_button:
//                        setFanSpeed("50");
//                        break;
//                    case R.id.fs_75_button:
//                        setFanSpeed("75");
//                        break;
//                    case R.id.fs_100_button:
//                        setFanSpeed("100");
//                        break;
//                }
        });
    }

    private int getTemperatureValue() {
        String temp = (String) mTemperature.getText();
        return Integer.parseInt(temp.substring(0, temp.indexOf('°')));
    }

    @Override
    public void onDeviceRefresh(Device device) {
        ACState state = (ACState) device.getState();

        String status = state.getStatus();

        mDevName.setText(getParsedName(device.getName()));

        mState.setText(getResources().getString(R.string.temp_state,
                status.equals("on")? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado),
                state.getTemperature()));

        mTemperature.setText(getResources().getString(R.string.temp,
                String.valueOf(state.getTemperature())));

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));

        mSwitch.setChecked(status.equals("on"));

        Log.v("damn", state.getVerticalSwing());
        //noinspection ConstantConditions
        mVertGroup.check(actionToIdMap.get(state.getVerticalSwing()));

        //noinspection ConstantConditions
        mHorGroup.check(actionToIdMap.get(state.getHorizontalSwing()));

        //noinspection ConstantConditions
        mFanSpeedGroup.check(actionToIdMap.get(state.getFanSpeed()));

        //noinspection ConstantConditions
        mTempGroup.check(actionToIdMap.get(state.getMode()));
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

    private void setUpMaps(){
        actionToIdMap = new HashMap<>();
        idToActionMap = new HashMap<>();

        // Mode
        actionToIdMap.put("cool", R.id.cold_button);
        idToActionMap.put(R.id.cold_button, "cool");

        actionToIdMap.put("heat", R.id.heat_button);
        idToActionMap.put(R.id.heat_button, "heat");

        actionToIdMap.put("fan", R.id.ventilation_button);
        idToActionMap.put(R.id.ventilation_button, "fan");

        // Vertical Swing
        actionToIdMap.put("auto", R.id.v_auto_button);
        idToActionMap.put(R.id.v_auto_button, "auto");

        actionToIdMap.put("22", R.id.v_22_button);
        idToActionMap.put(R.id.v_22_button, "22");

        actionToIdMap.put("45", R.id.v_45_button);
        idToActionMap.put(R.id.v_45_button, "45");

        actionToIdMap.put("67", R.id.v_67_button);
        idToActionMap.put(R.id.v_67_button, "67");

        actionToIdMap.put("90", R.id.v_90_button);
        idToActionMap.put(R.id.v_90_button, "90");

        // Horizontal Swing
        actionToIdMap.put("auto", R.id.h_auto_button);
        idToActionMap.put(R.id.h_auto_button, "auto");

        actionToIdMap.put("-90", R.id.h_n90_button);
        idToActionMap.put(R.id.h_n90_button, "-90");

        actionToIdMap.put("-45", R.id.h_n45_button);
        idToActionMap.put(R.id.h_n45_button, "-45");

        actionToIdMap.put("0", R.id.h_0_button);
        idToActionMap.put(R.id.h_0_button, "0");

        actionToIdMap.put("45", R.id.h_45_button);
        idToActionMap.put(R.id.h_45_button, "45");

        actionToIdMap.put("90", R.id.h_90_button);
        idToActionMap.put(R.id.h_90_button, "90");

        // Fan Speed
        actionToIdMap.put("auto", R.id.fs_auto_button);
        idToActionMap.put(R.id.fs_auto_button, "auto");

        actionToIdMap.put("25", R.id.fs_25_button);
        idToActionMap.put(R.id.fs_25_button, "25");

        actionToIdMap.put("50", R.id.fs_50_button);
        idToActionMap.put(R.id.fs_50_button, "50");

        actionToIdMap.put("75", R.id.fs_75_button);
        idToActionMap.put(R.id.fs_75_button, "75");

        actionToIdMap.put("100", R.id.fs_100_button);
        idToActionMap.put(R.id.fs_100_button, "100");
    }
}

