package com.example.hci_3.device_views;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;

import com.example.hci_3.api.DeviceStates.VacuumState;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Collections;

public class VacuumView extends DeviceView {

    private TextView mDevName, mState, mLocation;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private Spinner mSpinner;
    private MaterialButtonToggleGroup mStateGroup, mModeGroup;
    private ArrayAdapter<CharSequence> locationAdapter;

    public VacuumView(Context context) {
        super(context);
    }

    public VacuumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VacuumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.vacuum_view, this, true);

        mDevName = findViewById(R.id.vacuum_name);
        mLocation = findViewById(R.id.vacuum_location);
        mState = findViewById(R.id.onStateVacuum);
        cardView = findViewById(R.id.room_card);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);
        mSpinner = findViewById(R.id.locationSpinner);
        mStateGroup = findViewById(R.id.vacuum_onstate_group);
        mModeGroup = findViewById(R.id.vacuum_mode_group);
        locationAdapter = ArrayAdapter.createFromResource(context, R.array.vacuum_location, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        mSpinner.setAdapter(locationAdapter);

        extendBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.VISIBLE);
                // Falta rotar la flecha
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
            }
        });

        final String[] location = getResources().getStringArray(R.array.vacuum_location);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setLocation(location[arg2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mStateGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.on_button)
                    start();
                else if (checkedId == R.id.charge_button)
                    dock();
                else
                    pause();
            }
        });
        mModeGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.aspirar_button)
                    setMode("vacuum");
                else
                    setMode("mop");
            }
        });

    }

    @Override
    public void onDeviceRefresh(Device device) {

        VacuumState state = (VacuumState) device.getState();

        mDevName.setText(getParsedName(device.getName()));
        mState.setText(getResources().getString(R.string.state,
                state.getStatus().equals("active")? getResources().getString(R.string.activa) :state.getStatus().equals("docked")?
                        getResources().getString(R.string.cargandose) +  getResources().getString(R.string.batery_state, state.getBatteryLevel()) + "%":
                        getResources().getString(R.string.apagada)));

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));
    }

    private void setLocation(String roomid){ executeAction("setLocation",new ArrayList<>(Collections.singletonList(roomid)), this::handleError);}

    private void start(){
        executeAction("start", this::handleError);
    }
    private void pause(){
        executeAction("pause", this::handleError);
    }
    private void dock(){
        executeAction("dock", this::handleError);
    }
    private void setMode(String value){
        executeAction("setMode", new ArrayList<>(Collections.singletonList(value)), this::handleError);
    }
}