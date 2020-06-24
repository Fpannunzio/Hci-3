package com.example.hci_3.device_views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.DeviceState;
import com.example.hci_3.api.DeviceStates.SpeakerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class SpeakerView extends DeviceView {
    private TextView mDevName, mState, mLocation, mSongState;
    private Switch mSwitch;
    private ImageButton mPrevious, mNext, mPause;
    private Spinner mGenre;
    private SeekBar mSeekBar;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private SpeakerState state;
    private ListView mPlaylist;

    private ArrayAdapter<String> playlistAdapter;
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
        mSongState = findViewById(R.id.song_state);
        mPlaylist = findViewById(R.id.playlist);

        cardView = findViewById(R.id.room_card);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        playlistAdapter = new ArrayAdapter<>(context, android.R.layout.simple_expandable_list_item_1);
        genreAdapter = ArrayAdapter.createFromResource(context, R.array.genres, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        Device dev = device.getValue();

        //noinspection ConstantConditions
        state = (SpeakerState) dev.getState();

        model.addPollingState(dev, 1000).observe(getLifecycleOwner(), this::updateFrequentlyUpdatingState);

        mPlaylist.setAdapter(playlistAdapter);

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

        if (state.getStatus().equals("playing"))
            enableControlButtons();
        else
            disableControlButtons();

        mPause.setOnClickListener(v -> {
            if(state.getStatus().equals("playing")) {
                pause();
                mPause.setImageResource(R.drawable.ic_play);
                disableControlButtons();
            }
            else if(state.getStatus().equals("paused")) {
                resume();
                mPause.setImageResource(R.drawable.ic_pause);
                enableControlButtons();
            }
            else {
                mSwitch.setChecked(true);
                play();
                mPause.setImageResource(R.drawable.ic_pause);
                enableControlButtons();
            }
        });

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


        executeAction("getPlaylist", ((success, response) -> {
            List<Map<String, Object>> map;
            map = parseGetPlaylistResult(response);
            ArrayList<String> toPlaylist = new ArrayList<>();
            playlistAdapter.clear();
            for (Map<String, Object> song : map){
                //noinspection ConstantConditions
                toPlaylist.add(song.get("artist").toString().concat(" - ").concat(song.get("title").toString()));
            }
            playlistAdapter.addAll(toPlaylist);
            playlistAdapter.notifyDataSetChanged();
        }),this::handleError);
    }

    @Override
    public void onDeviceRefresh(Device device) {

        state = (SpeakerState) device.getState();

        mDevName.setText(getParsedName(device.getName()));

        if(!state.getStatus().equals("stopped"))
            mSwitch.setChecked(true);

        mLocation.setText(getResources().getString(R.string.disp_location,
                getParsedName(device.getRoom().getName()),
                device.getRoom().getHome().getName()));

        mSeekBar.setProgress(state.getVolume());

        // TODO: 6/23/2020 Falta la lista de reproduccion y traer el genero desde la api cuando arranca
    }

    private void disableControlButtons() {
        setImageButtonEnabled(context, false, mNext, R.drawable.ic_next);
        setImageButtonEnabled(context, false, mPrevious, R.drawable.ic_previous);
    }

    private void enableControlButtons() {
        setImageButtonEnabled(context, true, mNext, R.drawable.ic_next);
        setImageButtonEnabled(context, true, mPrevious, R.drawable.ic_previous);
    }

    public static void setImageButtonEnabled(Context context, boolean enabled, ImageButton item, int iconResId) {
        item.setEnabled(enabled);
        Drawable originalIcon = ResourcesCompat.getDrawable(context.getResources(), iconResId, null);
        Drawable icon = enabled ? originalIcon : convertDrawableToGrayScale(originalIcon);
        item.setImageDrawable(icon);
    }

    public static Drawable convertDrawableToGrayScale(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Drawable res = drawable.mutate();
        res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return res;
    }

    private void updateFrequentlyUpdatingState(DeviceState uncastedState){
        SpeakerState state = (SpeakerState) uncastedState;

        if (!state.getStatus().equals("stopped")) {
            mState.setText(getResources().getString(R.string.speaker_state,
                    state.getStatus().equals("playing") ? getResources().getString(R.string.reproduciendo) : getResources().getString(R.string.pausado), state.getSong().getTitle()
            ));
            mSongState.setVisibility(VISIBLE);
            mSongState.setText(state.getSong().getProgress());
        }else {
            mSongState.setVisibility(GONE);
            mState.setText(getResources().getString(R.string.parado));
        }
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
        executeAction("setVolume",new ArrayList<>(Collections.singletonList(volume)), this::handleError);
    }

    // Returns Map with the following keys: [ title, artist, album, duration ]
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseGetPlaylistResult(Object result) {

        List<Map<String, Object>> song;

        try {
            song = (List<Map<String, Object>>) result;
        }
        catch (Exception e) {
            throw new IllegalStateException("getPlaylist result is incompatible with current code. Was expecting List<Map<String, Object>>");
        }

        return song;
    }
}