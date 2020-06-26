package com.example.hci_3.device_views;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;

import com.example.hci_3.api.DeviceStates.DeviceState;
import com.example.hci_3.api.DeviceStates.VacuumState;
import com.example.hci_3.api.Room;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class VacuumView extends DeviceView {

    private TextView mDevName, mState, mLocation;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private Spinner mSpinner;
    private MaterialButtonToggleGroup mStateGroup, mModeGroup;
    private Map<String, Integer> actionToButtonMap;
    private ArrayAdapter<InfoRoom> locationAdapter;
    private InfoRoom currentRoom;
    private boolean inactive = false, charge = false;


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
        locationAdapter = new ArrayAdapter<>(context, R.layout.spinner_item);

        initActionToButtonMap();
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        if(isDeviceSetted > 1)
            return;
        isDeviceSetted++;

        mSpinner.setAdapter(locationAdapter);

        model.addPollingState(device.getValue(), 2000).observe(getLifecycleOwner(), this::updateFrequentlyUpdatingState);

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

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                currentRoom = locationAdapter.getItem(arg2);
                //noinspection ConstantConditions
                setLocation(currentRoom.getRoomId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mStateGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.on_button) {
                    VacuumState state = (VacuumState) device.getValue().getState();
                    if(state.getBatteryLevel() < 5){
                        mStateGroup.uncheck(R.id.on_button);
                        Toast.makeText(context, getResources().getString(R.string.turn_on_vacuum_error), Toast.LENGTH_SHORT).show();
                        if(state.getStatus().equals("inactive"))
                            inactive = true;
                        else
                            charge = true;
                    } else
                        start();
                } else if (checkedId == R.id.charge_button)
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

        String status = state.getStatus();

        mDevName.setText(device.getParsedName());

        mState.setText(getResources().getString(R.string.state,
                status.equals("active")? getResources().getString(R.string.activa) + " - " + getResources().getString(R.string.batery_state, state.getBatteryLevel()) + "%" : status.equals("docked")?
                        getResources().getString(R.string.cargandose) + " - " + getResources().getString(R.string.batery_state, state.getBatteryLevel()) + "%":
                        getResources().getString(R.string.apagada)));

        mLocation.setText(getResources().getString(R.string.disp_location,
                device.getRoom().getParsedName(),
                device.getRoom().getHome().getName()));

        //noinspection ConstantConditions
        mStateGroup.check(actionToButtonMap.get(status));

        //noinspection ConstantConditions
        mModeGroup.check(actionToButtonMap.get(state.getMode()));

        if (status.equals("active")) {
            mSpinner.setEnabled(true);
            mSpinner.setClickable(true);
        } else {
            mSpinner.setEnabled(false);
            mSpinner.setClickable(false);
        }

        if(state.getBatteryLevel() < 5 && state.getStatus().equals("active")) {
            Toast.makeText(context, getResources().getString(R.string.open_error), Toast.LENGTH_SHORT).show();
            dock();
        }

        currentRoom = (state.getLocation() != null)?
                new InfoRoom(state.getLocation().getParsedName(), state.getLocation().getId()) :
                new InfoRoom(device.getRoom().getParsedName(), device.getRoom().getId());

        if(locationAdapter.getCount() > 0)
            updateLocationSpinner();

        model.getRooms(device, rooms -> {
            locationAdapter.clear();
            locationAdapter.addAll(rooms.stream().map(room -> new InfoRoom(room.getParsedName(), room.getId())).collect(Collectors.toList()));
            locationAdapter.notifyDataSetChanged();

            updateLocationSpinner();
        }, this::handleError);
    }

    private void updateFrequentlyUpdatingState(DeviceState uncastedState) {
        if(inactive){
            mStateGroup.check(R.id.off_button);
            inactive = false;
        }
        else if (charge){
            mStateGroup.check(R.id.charge_button);
            charge = false;
        }
    }

    private void setLocation(String roomid){ executeAction("setLocation", new ArrayList<>(Collections.singletonList(roomid)), this::handleError);}

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

    private void updateLocationSpinner(){
        int newPosition = locationAdapter.getPosition(currentRoom);

        if(newPosition == -1){
            //noinspection ConstantConditions
            Room room = device.getValue().getRoom();

            currentRoom = new InfoRoom(room.getParsedName(), room.getId());
            newPosition = locationAdapter.getPosition(currentRoom);
            Toast.makeText(context, getResources().getString(R.string.invalid_vacuum_location), Toast.LENGTH_SHORT).show();
        }
        mSpinner.setSelection(newPosition);
    }

    private void initActionToButtonMap(){
        actionToButtonMap = new HashMap<>();
        actionToButtonMap.put("vacuum", R.id.aspirar_button);
        actionToButtonMap.put("mop", R.id.trapear_button);
        actionToButtonMap.put("active", R.id.on_button);
        actionToButtonMap.put("inactive", R.id.off_button);
        actionToButtonMap.put("docked", R.id.charge_button);
    }

    private static class InfoRoom {
        private String roomName, roomId;

        public InfoRoom(String roomName, String roomId) {
            this.roomName = roomName;
            this.roomId = roomId;
        }

        public String getRoomId(){
            return roomId;
        }

        @NonNull
        @Override
        public String toString() {
            return roomName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InfoRoom)) return false;
            InfoRoom infoRoom = (InfoRoom) o;
            return Objects.equals(roomId, infoRoom.roomId);
        }
    }
}