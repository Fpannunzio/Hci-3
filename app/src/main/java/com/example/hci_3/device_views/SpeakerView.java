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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.SpeakerState;

import java.util.ArrayList;
import java.util.Collections;


public class SpeakerView extends DeviceView {
    private TextView mDevName, mState, mLocation;
    private Switch mSwitch;
    private ImageButton mPrevious, mNext, mPause;
    private Spinner mGenre;
    private SeekBar mSeekBar;
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
        mPrevious = findViewById(R.id.speaker_previous);
        mPause = findViewById(R.id.speaker_pause);
        mNext = findViewById(R.id.speaker_next);
        mGenre = findViewById(R.id.speaker_spinner);
        mSeekBar = findViewById(R.id.speaker_seekbar);

        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        genreAdapter = ArrayAdapter.createFromResource(context, R.array.genres, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        mGenre.setAdapter(genreAdapter);
        final String[] generos = getResources().getStringArray(R.array.genres);

        mGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setGenre(generos[arg2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

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

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                play();
                mPause.setImageResource(R.drawable.ic_pause);
            }
            else {
                stop();
                mPause.setImageResource(R.drawable.ic_play);
            }
        });

        mNext.setOnClickListener(v -> nextSong());

        mPrevious.setOnClickListener(v -> previousSong());

        mSeekBar.setMax(10);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setVolume(progress);
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

        SpeakerState state = (SpeakerState) device.getState();

        mDevName.setText(getParsedName(device.getName()));

        if(!state.getStatus().equals("stopped"))
            mSwitch.setChecked(true);

        mState.setText(getResources().getString(R.string.speaker_state,
                state.getStatus().equals("playing")? getResources().getString(R.string.reproduciendo) :state.getStatus().equals("stopped")?
                        getResources().getString(R.string.parado):getResources().getString(R.string.pausado), state.getGenre()
               ));

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));

        mSeekBar.setProgress(state.getVolume());

        mPause.setOnClickListener(v -> {
            if(state.getStatus().equals("playing")) {
                pause();
                mPause.setImageResource(R.drawable.ic_play);
            }
            else if(state.getStatus().equals("paused")) {
                resume();
                mPause.setImageResource(R.drawable.ic_pause);
            }
            else {
                mSwitch.setChecked(true);
                play();
                mPause.setImageResource(R.drawable.ic_pause);

            }


        });

    }

    private void play(){
        executeAction("play", this::handleError);
    }

    private void stop(){
        executeAction("stop", this::handleError);
    }
    private void nextSong(){
        executeAction("nextSong", this::handleError);
    }

    private void previousSong(){
        executeAction("previousSong", this::handleError);
    }

    private void pause(){
        executeAction("pause", this::handleError);
    }
    private void resume(){
        executeAction("resume", this::handleError);
    }

    private void setGenre(String genre){ executeAction("setGenre",new ArrayList<>(Collections.singletonList(genre)), this::handleError);}

    private void setVolume(int volume){
        executeAction("setVolume",new ArrayList<>(Collections.singletonList(volume/10)), this::handleError);
    }
}