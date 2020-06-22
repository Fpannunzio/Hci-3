package com.example.hci_3.device_views;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.ACState;
import com.example.hci_3.api.DeviceStates.LampState;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class LampView extends DeviceView {

    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private TextView mDevName;
    private TextView mState;
    private TextView mLocation;
    private ColorPickerView colorPickerView;
    private View currentColor;
    private Switch mSwitch;
    private SeekBar mBrightness;

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
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.lamp_view, this, true);

        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);
        mDevName = findViewById(R.id.lamp_name);
        mLocation = findViewById(R.id.lamp_location);
        mState = findViewById(R.id.onStateLamp);
        currentColor = findViewById(R.id.currentColor);
        colorPickerView = findViewById(R.id.colorPickerView);
        mSwitch = findViewById(R.id.lamp_switch);
        mBrightness = findViewById(R.id.brightness);
    }

    @Override
    public void onDeviceRefresh(Device device) {
        LampState state = (LampState) device.getState();

        mDevName.setText(getParsedName(device.getName()));

        mState.setText(getResources().getString(R.string.lamp_state, state.getStatus().equals("on") ? getResources().getString(R.string.prendido):getResources().getString(R.string.apagado)));

        mSwitch.setChecked(state.getStatus().equals("on"));

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        @SuppressWarnings("ConstantConditions")
        LampState state = (LampState) device.getValue().getState();

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

        colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(int color, boolean fromUser) {
                currentColor.setBackgroundColor(color);
                color &= 0x00FFFFFF; // Mascara para sacar el alpha al color
                String hexColor = Integer.toHexString(color);
                setColor(hexColor);
                state.setColor(hexColor);
            }
        });

        mSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (isChecked)
                turnOn();
            else
                turnOff();
        }));

        mBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                state.setBrightness(progress);
                setBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void turnOff() {
        executeAction("turnOff", this::handleError);
    }

    private void turnOn() {
        executeAction("turnOn", this::handleError);
    }

    private void setColor(String value){
        executeAction("setColor", new ArrayList<>(Collections.singletonList(value)), this::handleError);
    }

    private void setBrightness(int value){
        executeAction("setBrightness", new ArrayList<>(Collections.singletonList(value)), this::handleError);
    }
}