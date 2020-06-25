package com.example.hci_3.device_views;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
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
    private Map<String, Integer> modeToIdMap;
    private Map<String, Integer> verticalToIdMap;
    private Map<String, Integer> horizontalToIdMap;
    private Map<String, Integer> fanSpeedToIdMap;
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

        extendBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE){
                android.transition.TransitionManager.beginDelayedTransition(cardView, new android.transition.AutoTransition());
                expandableLayout.setVisibility(View.VISIBLE);
                extendBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
                extendBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
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
        });

        mVertGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setVerticalSwing(idToActionMap.get(checkedId));
        });

        mHorGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setHorizontalSwing(idToActionMap.get(checkedId));
        });

        mFanSpeedGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setFanSpeed(idToActionMap.get(checkedId));
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
                status.equals("on")? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado), state.getMode(),
                state.getTemperature()));

        mTemperature.setText(getResources().getString(R.string.temp,
                String.valueOf(state.getTemperature())));

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));

        mSwitch.setChecked(status.equals("on"));

        Log.v("damn", state.getVerticalSwing());
        //noinspection ConstantConditions
        mVertGroup.check(verticalToIdMap.get(state.getVerticalSwing()));

        //noinspection ConstantConditions
        mHorGroup.check(horizontalToIdMap.get(state.getHorizontalSwing()));

        //noinspection ConstantConditions
        mFanSpeedGroup.check(fanSpeedToIdMap.get(state.getFanSpeed()));

        //noinspection ConstantConditions
        mTempGroup.check(modeToIdMap.get(state.getMode()));
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
        modeToIdMap = new HashMap<>();
        verticalToIdMap = new HashMap<>();
        horizontalToIdMap = new HashMap<>();
        fanSpeedToIdMap = new HashMap<>();
        idToActionMap = new HashMap<>();

        // Mode
        modeToIdMap.put("cool", R.id.cold_button);
        idToActionMap.put(R.id.cold_button, "cool");

        modeToIdMap.put("heat", R.id.heat_button);
        idToActionMap.put(R.id.heat_button, "heat");

        modeToIdMap.put("fan", R.id.ventilation_button);
        idToActionMap.put(R.id.ventilation_button, "fan");

        // Vertical Swing
        verticalToIdMap.put("auto", R.id.v_auto_button);
        idToActionMap.put(R.id.v_auto_button, "auto");

        verticalToIdMap.put("22", R.id.v_22_button);
        idToActionMap.put(R.id.v_22_button, "22");

        verticalToIdMap.put("45", R.id.v_45_button);
        idToActionMap.put(R.id.v_45_button, "45");

        verticalToIdMap.put("67", R.id.v_67_button);
        idToActionMap.put(R.id.v_67_button, "67");

        verticalToIdMap.put("90", R.id.v_90_button);
        idToActionMap.put(R.id.v_90_button, "90");

        // Horizontal Swing
        horizontalToIdMap.put("auto", R.id.h_auto_button);
        idToActionMap.put(R.id.h_auto_button, "auto");

        horizontalToIdMap.put("-90", R.id.h_n90_button);
        idToActionMap.put(R.id.h_n90_button, "-90");

        horizontalToIdMap.put("-45", R.id.h_n45_button);
        idToActionMap.put(R.id.h_n45_button, "-45");

        horizontalToIdMap.put("0", R.id.h_0_button);
        idToActionMap.put(R.id.h_0_button, "0");

        horizontalToIdMap.put("45", R.id.h_45_button);
        idToActionMap.put(R.id.h_45_button, "45");

        horizontalToIdMap.put("90", R.id.h_90_button);
        idToActionMap.put(R.id.h_90_button, "90");

        // Fan Speed
        fanSpeedToIdMap.put("auto", R.id.fs_auto_button);
        idToActionMap.put(R.id.fs_auto_button, "auto");

        fanSpeedToIdMap.put("25", R.id.fs_25_button);
        idToActionMap.put(R.id.fs_25_button, "25");

        fanSpeedToIdMap.put("50", R.id.fs_50_button);
        idToActionMap.put(R.id.fs_50_button, "50");

        fanSpeedToIdMap.put("75", R.id.fs_75_button);
        idToActionMap.put(R.id.fs_75_button, "75");

        fanSpeedToIdMap.put("100", R.id.fs_100_button);
        idToActionMap.put(R.id.fs_100_button, "100");
    }
}

