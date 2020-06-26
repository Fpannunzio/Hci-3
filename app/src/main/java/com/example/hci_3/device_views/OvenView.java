package com.example.hci_3.device_views;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
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
import com.example.hci_3.api.DeviceStates.OvenState;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OvenView extends DeviceView {
    private TextView mDevName, mState, mTemperature, mLocation;
    private Switch mSwitch;
    private ImageButton mMinus, mPlus;
    private MaterialButtonToggleGroup mFont, mGrill, mConvection;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private Map<String, Integer> modeToIdMap;
    private Map<String, Integer> grillToIdMap;
    private Map<String, Integer> conventionalToIdMap;
    private Map<String, Integer> conventionalToStringMap;
    private Map<Integer, String> idToActionMap;

    public OvenView(Context context) {
        super(context);
    }

    public OvenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OvenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.oven_view, this, true);

        mLocation = findViewById(R.id.oven_location);
        mDevName = findViewById(R.id.oven_name);
        mTemperature = findViewById(R.id.oven_temp);
        mSwitch = findViewById(R.id.oven_switch);
        mState = findViewById(R.id.oven_state);
        mMinus = findViewById(R.id.oven_minus);
        mPlus = findViewById(R.id.oven_plus);

        cardView = findViewById(R.id.room_card);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        mFont = findViewById(R.id.heat_source_group);
        mGrill = findViewById(R.id.grill_toggle_group);
        mConvection = findViewById(R.id.conventional_toggle_group);

        setUpMaps();
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        if(isDeviceSetted > 1)
            return;
        isDeviceSetted++;

        mFont.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setHeat(idToActionMap.get(checkedId));
        });

        mGrill.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setGrill(idToActionMap.get(checkedId));
        });

        mConvection.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked)
                setConvection(idToActionMap.get(checkedId));
        });

        extendBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.VISIBLE);
                extendBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
                extendBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }
        });

        mMinus.setOnClickListener(v -> {
            int temp = getTemperatureValue() - 5;

            if (temp >= 90)
                setTemperature(temp);
            else
                Toast.makeText(context, getResources().getString(R.string.invalid_temp), Toast.LENGTH_SHORT).show();
        });

        mPlus.setOnClickListener(v -> {
            int temp = getTemperatureValue() + 5;

            if (temp <= 230)
                setTemperature(temp);
            else
                Toast.makeText(context, getResources().getString(R.string.invalid_temp), Toast.LENGTH_SHORT).show();

        });

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                turnOn();
            else
                turnOff();
        });
    }

    private int getTemperatureValue() {
        String temp = (String) mTemperature.getText();
        return Integer.parseInt(temp.substring(0, temp.indexOf('°')));
    }

    @Override
    public void onDeviceRefresh(Device device) {
        OvenState state = (OvenState) device.getState();

        mDevName.setText(device.getParsedName());

        mLocation.setText(getResources().getString(R.string.disp_location,
                device.getRoom().getParsedName(),
                device.getRoom().getHome().getName()));

        mState.setText(getResources().getString(R.string.temp_state,
                state.getStatus().equals("on") ? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado), getResources().getString(conventionalToStringMap.get(state.getHeat())),
                ((OvenState) device.getState()).getTemperature()));

        mTemperature.setText(getResources().getString(R.string.temp,
                String.valueOf(state.getTemperature())));

        mSwitch.setChecked(state.getStatus().equals("on"));

        mFont.check(modeToIdMap.get(state.getHeat()));

        mGrill.check(grillToIdMap.get(state.getGrill()));

        mConvection.check(conventionalToIdMap.get(state.getConvection()));
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

    private void setHeat(String heatMode) {
        executeAction("setHeat", new ArrayList<>(Collections.singletonList(heatMode)), this::handleError);
    }

    private void setGrill(String grillMode) {
        executeAction("setGrill", new ArrayList<>(Collections.singletonList(grillMode)), this::handleError);
    }

    private void setConvection(String convectionMode) {
        executeAction("setConvection", new ArrayList<>(Collections.singletonList(convectionMode)), this::handleError);
    }

    private void setUpMaps(){
        modeToIdMap = new HashMap<>();
        grillToIdMap = new HashMap<>();
        conventionalToIdMap = new HashMap<>();
        conventionalToStringMap = new HashMap<>();
        idToActionMap = new HashMap<>();

        // Heat
        modeToIdMap.put("conventional", R.id.conventional_button);
        conventionalToStringMap.put("conventional", R.string.oven_conventional);
        idToActionMap.put(R.id.conventional_button, "conventional");

        modeToIdMap.put("bottom", R.id.bottom_button);
        conventionalToStringMap.put("bottom", R.string.oven_bottom);
        idToActionMap.put(R.id.bottom_button, "bottom");

        modeToIdMap.put("top", R.id.top_button);
        conventionalToStringMap.put("top", R.string.oven_top);
        idToActionMap.put(R.id.top_button, "top");

        //grill mode
        grillToIdMap.put("off", R.id.grill_off_button);
        idToActionMap.put(R.id.grill_off_button, "off");

        grillToIdMap.put("large", R.id.grill_comp);
        idToActionMap.put(R.id.grill_comp, "large");

        grillToIdMap.put("eco", R.id.grill_eco_button);
        idToActionMap.put(R.id.grill_eco_button, "eco");

        // Horizontal Swing
        conventionalToIdMap.put("normal", R.id.convection_con_button);
        idToActionMap.put(R.id.convection_con_button, "normal");

        conventionalToIdMap.put("eco", R.id.convection_eco_button);
        idToActionMap.put(R.id.convection_eco_button, "eco");

        conventionalToIdMap.put("off", R.id.convection_off_button);
        idToActionMap.put(R.id.convection_off_button, "off");
    }
}