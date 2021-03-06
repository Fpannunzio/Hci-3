package com.example.hci_3.device_views;

import android.content.Context;
import android.graphics.Color;
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
import com.example.hci_3.api.DeviceStates.LampState;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;

import java.util.ArrayList;
import java.util.Collections;

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

        cardView = findViewById(R.id.room_card);
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

        mDevName.setText(device.getParsedName());

        mState.setText(getResources().getString(R.string.lamp_state, state.getStatus().equals("on") ? getResources().getString(R.string.prendido)
                + " - " + getResources().getString(R.string.brillo) + ": " + state.getBrightness() + "%"
                :getResources().getString(R.string.apagado)));

        mSwitch.setChecked(state.getStatus().equals("on"));

        mBrightness.setProgress(state.getBrightness());

        colorPickerView.selectByHsv(getColor(state.getColor()));

        currentColor.setBackgroundColor(getColor(state.getColor()));

        mLocation.setText(getResources().getString(R.string.disp_location,
                device.getRoom().getParsedName(),
                device.getRoom().getHome().getName()));
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        if(isDeviceSetted > 1)
            return;
        isDeviceSetted++;

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

        colorPickerView.setColorListener((ColorListener) (color, fromUser) -> {
            if(color == -260)
                color = getColor(((LampState) device.getValue().getState()).getColor());

            color &= 0x00FFFFFF; // Mascara para sacar el alpha al color
            String hexColor = Integer.toHexString(color);
            setColor(hexColor);
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
                ((LampState) device.getValue().getState()).setBrightness(progress);
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

    private int getColor(String hex){
        StringBuilder hexBuilder = new StringBuilder(hex);

        while(hexBuilder.length() < 6)
            hexBuilder.insert(0, '0');
        hexBuilder.insert(0, "#");

        return Color.parseColor(hexBuilder.toString());
    }
}