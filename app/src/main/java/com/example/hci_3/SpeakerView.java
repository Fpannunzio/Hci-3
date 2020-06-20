package com.example.hci_3;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.hci_3.api.Device;

public class SpeakerView extends DeviceView {
    private TextView mDevName, mState, mTemperature, mLocation;
    private Switch mSwitch;
    private ImageButton mMinus, mPlus, mPause;
    private Spinner mGenre;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;

    private ArrayAdapter<CharSequence> genreAdapter;

    public SpeakerView(Context context) {
        super(context);
    }

    public SpeakerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpeakerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.speaker_view, this, true);

        mLocation = findViewById(R.id.speaker_location);
        mDevName = findViewById(R.id.speaker_name);
        mSwitch = findViewById(R.id.speaker_switch);
        mState = findViewById(R.id.speaker_state);
        mMinus = findViewById(R.id.speaker_minus);
        mPause = findViewById(R.id.speaker_pause);
        mPlus = findViewById(R.id.speaker_plus);
        mGenre = findViewById(R.id.genreSpinner);

        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        genreAdapter = ArrayAdapter.createFromResource(context, R.array.genres, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        mGenre.setAdapter(genreAdapter);

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
    }

    @Override
    public void onDeviceRefresh(Device device) {

    }
}