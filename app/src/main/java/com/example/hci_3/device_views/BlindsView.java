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

import java.util.ArrayList;
import java.util.Collections;

public class BlindsView extends DeviceView {

    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private TextView mDevName, mState,  mLocation, mLevel;
    private Button mOpen, mClose;
    private SeekBar mSeekBar;
    private int level = 0;
    private BlindsState state;

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
                // Falta rotar la flecha
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(state.getStatus().equals("opened")) {
                    level = progress;
                    mLevel.setText(getResources().getString(R.string.blinds_level, level) + "%");
                    setLevel();
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

    @Override
    public void onDeviceRefresh(Device device) {
        state = (BlindsState) device.getState();

        mDevName.setText(getParsedName(device.getName()));

        mState.setText(getResources().getString(R.string.blinds_state,
                state.getStatus().equals("opened")? getResources().getString(R.string.abierta) :state.getStatus().equals("opening")?
                        getResources().getString(R.string.abriendose): state.getStatus().equals("closing")?
                        getResources().getString(R.string.cerrandose) : getResources().getString(R.string.cerrada), state.getCurrentLevel()) + "%");

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));

        if(state.getStatus().equals("opened")) {
            mOpen.setClickable(false);
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mOpen.setBackgroundColor(Color.parseColor(colorHex));
            mClose.setClickable(true);
            colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtons) & 0x00ffffff);
            mClose.setBackgroundColor(Color.parseColor(colorHex));
        }
        else if(state.getStatus().equals("closing") || state.getStatus().equals("opening")) {
            mClose.setClickable(false);
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mClose.setBackgroundColor(Color.parseColor(colorHex));
            mOpen.setClickable(false);
            mOpen.setBackgroundColor(Color.parseColor(colorHex));
        }
        else{
            mClose.setClickable(false);
            String colorHex = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.colorButtonsDisabled) & 0x00ffffff);
            mClose.setBackgroundColor(Color.parseColor(colorHex));
            mOpen.setClickable(true);
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
                close();
            } else
                Toast.makeText(context, getResources().getString(R.string.blinds_close_error), Toast.LENGTH_LONG).show();
        });

        mSeekBar.setProgress(state.getLevel());
        mLevel.setText(state.getLevel() + "%");
    }
    private void open(){
        executeAction("open", this::handleError);
    }

    private void close(){
        executeAction("close", this::handleError);
    }

    private void setLevel(){ executeAction("setLevel", new ArrayList<>(Collections.singletonList(level)),this::handleError);
    }

}