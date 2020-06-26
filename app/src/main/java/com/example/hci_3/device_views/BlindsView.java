package com.example.hci_3.device_views;

import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.BlindsState;
import com.example.hci_3.api.DeviceStates.DeviceState;

import java.util.ArrayList;
import java.util.Collections;

public class BlindsView extends DeviceView {

    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private TextView mDevName, mState,  mLocation, mLevel;
    private Button mOpen, mClose;
    private SeekBar mSeekBar;
    private int level;

    public BlindsView(Context context) {
        super(context);
    }

    public BlindsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlindsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.blinds_layout, this, true);

        cardView = findViewById(R.id.room_card);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);
        mDevName = findViewById(R.id.blinds_name);
        mLocation = findViewById(R.id.blinds_location);
        mState = findViewById(R.id.onStateBlinds);
        mOpen = findViewById(R.id.blinds_open);
        mClose = findViewById(R.id.blinds_close);
        mSeekBar = findViewById(R.id.blinds_seekbar);
        mLevel = findViewById(R.id.blinds_level);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

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



        model.addPollingState(device.getValue(), 1000).observe(getLifecycleOwner(), this::updateFrequentlyUpdatingState);
    }

    @Override
    public void onDeviceRefresh(Device device) {
        BlindsState state = (BlindsState) device.getState();

        mDevName.setText(getParsedName(device.getName()));

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));

    }
    private void open(){
        executeAction("open", this::handleError);
    }

    private void close(){
        executeAction("close", this::handleError);
    }

    private void setLevel(int level){ executeAction("setLevel", new ArrayList<>(Collections.singletonList(level)),this::handleError);
    }

    private void updateFrequentlyUpdatingState(DeviceState uncastedState){
        BlindsState state = (BlindsState) uncastedState;

        mState.setText(getResources().getString(R.string.blinds_state,
                state.getStatus().equals("opened")? getResources().getString(R.string.abierta) :state.getStatus().equals("opening")?
                        getResources().getString(R.string.abriendose): state.getStatus().equals("closing")?
                        getResources().getString(R.string.cerrandose) : getResources().getString(R.string.cerrada), state.getCurrentLevel()) + "%");

        if (state.getStatus().equals("opened")) {
            mState.setText(getResources().getString(R.string.state, getResources().getString(R.string.abierta)));
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mOpen.setBackgroundColor(Color.parseColor(colorHex));
            colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtons) & 0x00ffffff);
            mClose.setBackgroundColor(Color.parseColor(colorHex));
            mSeekBar.setEnabled(true);
        }
        else if (state.getStatus().equals("closing") || state.getStatus().equals("opening")) {
            mState.setText(getResources().getString(R.string.blinds_state, state.getStatus().equals("opening")?
                            getResources().getString(R.string.abriendose): getResources().getString(R.string.cerrandose), state.getCurrentLevel()) + "%");
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mClose.setBackgroundColor(Color.parseColor(colorHex));
            mOpen.setBackgroundColor(Color.parseColor(colorHex));
            mSeekBar.setEnabled(false);
            mSeekBar.setProgress(state.getCurrentLevel());
            mLevel.setText(getResources().getString(R.string.blinds_level, state.getCurrentLevel()) + "%");
        }
        else {
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mClose.setBackgroundColor(Color.parseColor(colorHex));
            colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtons) & 0x00ffffff);
            mOpen.setBackgroundColor(Color.parseColor(colorHex));
        }

        mOpen.setOnClickListener(v -> {
            if (state.getStatus().equals("closed")) {
                open();
            } else
                Toast.makeText(context, getResources().getString(R.string.blinds_open_error), Toast.LENGTH_LONG).show();
        });

        mClose.setOnClickListener(v -> {
            if (state.getStatus().equals("opened")) {
                setLevel(level);
                close();
            } else
                Toast.makeText(context, getResources().getString(R.string.blinds_close_error), Toast.LENGTH_LONG).show();
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(state.getStatus().equals("opened")) {
                    Log.v("progress", String.valueOf(progress));
                    mLevel.setText(getResources().getString(R.string.blinds_level, progress) + "%");
                    setLevel(level);
                    level = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

}