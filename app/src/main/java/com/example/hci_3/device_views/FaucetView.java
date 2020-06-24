package com.example.hci_3.device_views;

import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;

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
import com.example.hci_3.api.DeviceStates.FaucetState;

import java.util.ArrayList;
import java.util.Objects;

public class FaucetView extends DeviceView {

    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private Integer quantity = 1;
    private String unit;


    private TextView mDevName, mState,  mLocation, mAmount;
    private Button mOpen, mDispense;
    private Spinner mUnitSpinner;
    private SeekBar mSeekBar;
    private ArrayAdapter<CharSequence> unitAdapter;

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

        unitAdapter = ArrayAdapter.createFromResource(context, R.array.faucet_units, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        mAmount.setText(getResources().getString(R.string.faucet_amount, quantity, unit));
        FaucetState state = (FaucetState) Objects.requireNonNull(device.getValue()).getState();
        mUnitSpinner.setAdapter(unitAdapter);

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

        final String[] faucetUnits = getResources().getStringArray(R.array.faucet_units);

        mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                unit = (faucetUnits[arg2]);
                state.setUnit(unit);
                mAmount.setText(getResources().getString(R.string.faucet_amount, quantity, unit));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                quantity = progress;
                if(quantity == 0)
                    quantity=1;
                state.setQuantity(quantity);
                mAmount.setText(getResources().getString(R.string.faucet_amount, quantity, unit));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onDeviceRefresh(Device device) {

        FaucetState state = (FaucetState) device.getState();

        mDevName.setText(getParsedName(device.getName()));

        mState.setText(getResources().getString(R.string.state,
                state.getStatus().equals("opened")? getResources().getString(R.string.abierto) : getResources().getString(R.string.cerrado)));

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));

        if(state.getStatus().equals("opened")) {
            mDispense.setClickable(false);
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mDispense.setBackgroundColor(Color.parseColor(colorHex));
        }
        else {
            mDispense.setClickable(true);
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtons) & 0x00ffffff);
            mDispense.setBackgroundColor(Color.parseColor(colorHex));
        }

        mOpen.setOnClickListener(v -> {
            FaucetState aux = (FaucetState) device.getState();
                if (aux.getStatus().equals("closed")) {
                    open();
                } else
                    close();
        });
        mDispense.setOnClickListener(v -> {
            FaucetState aux = (FaucetState) device.getState();
            if (aux.getStatus().equals("closed")) {
                dispense();
            } else
                Toast.makeText(context, getResources().getString(R.string.dispense_error), Toast.LENGTH_LONG).show();
        });

        mOpen.setText(getResources().getString(R.string.state,
                state.getStatus().equals("opened")? getResources().getString(R.string.cerrar) : getResources().getString(R.string.abrir)));

        mAmount.setText(getResources().getString(R.string.faucet_amount, quantity, unit));

    }

    private void open(){
        executeAction("open", this::handleError);
    }

    private void close(){
        executeAction("close", this::handleError);
    }

    private void dispense(){
        ArrayList<Object> aux= new ArrayList<>();
        aux.add(quantity);
        aux.add(unit);
        executeAction("dispense",aux,this::handleError);
    }
}