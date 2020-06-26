package com.example.hci_3.device_views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.DeviceState;
import com.example.hci_3.api.DeviceStates.FaucetState;


import java.util.ArrayList;
import java.util.Objects;

public class FaucetView extends DeviceView {

    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private boolean flag;
    private Integer aux;
    private TextView mDevName, mState,  mLocation, mAmount;
    private Button mOpen, mDispense;
    private Spinner mUnitSpinner;
    private SeekBar mSeekBar;
    private ArrayAdapter<CharSequence> unitAdapter;
    private SharedPreferences sp;

    public FaucetView(Context context) {
        super(context);
    }

    public FaucetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FaucetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.faucet_view, this, true);

        flag = false;

        sp = context.getSharedPreferences("misc", Context.MODE_PRIVATE);
        mDevName = findViewById(R.id.faucet_name);
        mLocation = findViewById(R.id.faucet_location);
        mState = findViewById(R.id.onStateFaucet);
        mOpen = findViewById(R.id.faucet_open);
        mDispense = findViewById(R.id.faucet_dispense);
        mAmount = findViewById(R.id.faucet_amount);
        mSeekBar = findViewById(R.id.faucet_seekBar);

        cardView = findViewById(R.id.room_card);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        mUnitSpinner = findViewById(R.id.unitSpinner);
        unitAdapter = ArrayAdapter.createFromResource(context, R.array.faucet_units, R.layout.spinner_item);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        if(isDeviceSetted > 1)
            return;
        isDeviceSetted++;

        FaucetState state = (FaucetState) Objects.requireNonNull(device.getValue()).getState();

        mUnitSpinner.setAdapter(unitAdapter);

        model.addPollingState(device.getValue(), 1000).observe(getLifecycleOwner(), this::updateFrequentlyUpdatingState);

        extendBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.VISIBLE);
                extendBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
                extendBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
            }
        });

        final String[] faucetUnits = getResources().getStringArray(R.array.faucet_units);

        mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String unitSelected = faucetUnits[arg2];
                mAmount.setText(getResources().getString(R.string.faucet_amount, mSeekBar.getProgress() == 0? 1: mSeekBar.getProgress(), unitSelected));

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("faucet_unit_value", unitSelected);
                editor.apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAmount.setText(getResources().getString(R.string.faucet_amount, progress== 0? 1: progress, mUnitSpinner.getSelectedItem()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(state.getUnit() != null)
            mUnitSpinner.setSelection(unitAdapter.getPosition(state.getUnit()));

        else
            mUnitSpinner.setSelection(unitAdapter.getPosition(sp.getString("faucet_unit_value", faucetUnits[0])));
    }

    @Override
    public void onDeviceRefresh(Device device) {

        FaucetState state = (FaucetState) device.getState();

        mDevName.setText(device.getParsedName());

        mLocation.setText(getResources().getString(R.string.disp_location,
                device.getRoom().getParsedName(),
                device.getRoom().getHome().getName()));

        mOpen.setText(getResources().getString(R.string.state,
                state.getStatus().equals("opened")? getResources().getString(R.string.cerrar) : getResources().getString(R.string.abrir)));
    }

    private void open(){
        executeAction("open", this::handleError);
    }

    private void close(){
        executeAction("close", this::handleError);
    }

    private void dispense(){
        ArrayList<Object> aux= new ArrayList<>();
        aux.add(mSeekBar.getProgress());
        aux.add(mUnitSpinner.getSelectedItem());
        executeAction("dispense",aux,this::handleError);
    }

    private void updateFrequentlyUpdatingState(DeviceState uncastedState) {
        FaucetState state = (FaucetState) uncastedState;
        state.setUnit((String) mUnitSpinner.getSelectedItem());


        mState.setText(getResources().getString(R.string.state,
                state.getStatus().equals("opened")? state.getDispensedQuantity() != null? getResources().getString(R.string.dispensando, state.getDispensedQuantity().intValue(), state.getUnit()) : getResources().getString(R.string.abierto) : getResources().getString(R.string.cerrado)));

        if (state.getStatus().equals("opened")) {
            mDispense.setClickable(false);
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mDispense.setBackgroundColor(Color.parseColor(colorHex));
            if(state.getDispensedQuantity() == null)
                mState.setText(getResources().getString(R.string.abierto));
            else {
                flag = true;
                mState.setText(getResources().getString(R.string.dispensando, state.getDispensedQuantity().intValue(), state.getUnit()));
                mSeekBar.setProgress(state.getQuantity() - state.getDispensedQuantity().intValue());
                aux = state.getQuantity() - state.getDispensedQuantity().intValue();
                mAmount.setText(getResources().getString(R.string.faucet_amount, aux, mUnitSpinner.getSelectedItem()));
                mUnitSpinner.setEnabled(false);
            }
        } else {
            mState.setText(getResources().getString(R.string.cerrado));
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtons) & 0x00ffffff);
            mDispense.setBackgroundColor(Color.parseColor(colorHex));
            if(flag) {
                mAmount.setText(getResources().getString(R.string.faucet_amount, aux == 0 ? 1 : aux, mUnitSpinner.getSelectedItem()));
                flag=false;
            }
            mUnitSpinner.setEnabled(true);
            mUnitSpinner.setSelection(unitAdapter.getPosition(state.getUnit()));
        }
        mOpen.setOnClickListener(v -> {
            //FaucetState aux = (FaucetState) device.getState();
            if (state.getStatus().equals("closed")) {
                open();
            } else {
                close();
            }
        });
        mDispense.setOnClickListener(v -> {
            FaucetState aux = (FaucetState) uncastedState;
            if (aux.getStatus().equals("closed")) {
                dispense();
            } else
                Toast.makeText(context, getResources().getString(R.string.dispense_error), Toast.LENGTH_SHORT).show();
        });
    }

}