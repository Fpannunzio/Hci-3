package com.example.hci_3.device_views;

import android.content.Context;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
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

    private static final String[] genres = {"pop", "country", "rock", "classical", "dance", "latina"};

    private TextView mDevName, mState, mLocation, mSongState, mPlaylist;
    private TextView[] mPlaylistItems;
    private Switch mSwitch;
    private ImageButton mPrevious, mNext, mPause;
    private Spinner mGenre;
    private SeekBar mSeekBar;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private SpeakerState state;
    private List<Map<String, Object>> playlist;
    private int currentSong = -1;

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
        mPlaylistItems = new TextView[3];
        mPlaylistItems[0] = findViewById(R.id.playlist_item_1);
        mPlaylistItems[1] = findViewById(R.id.playlist_item_2);
        mPlaylistItems[2] = findViewById(R.id.playlist_item_3);
        mPlaylist = findViewById(R.id.playlist_title);

        cardView = findViewById(R.id.room_card);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        genreAdapter = ArrayAdapter.createFromResource(context, R.array.genres, android.R.layout.simple_spinner_item);
        mGenre.setAdapter(genreAdapter);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        if(isDeviceSetted > 1)
            return;
        isDeviceSetted++;

        Device dev = device.getValue();

        //noinspection ConstantConditions
        state = (SpeakerState) dev.getState();

        model.addPollingState(dev, 1000).observe(getLifecycleOwner(), this::updateFrequentlyUpdatingState);

        mGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setGenre(genres[arg2]);
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
            if (isChecked)
                play();
            else
                stop();
        });


        mNext.setOnClickListener(v -> nextSong());

        mPrevious.setOnClickListener(v -> previousSong());

        mPause.setOnClickListener(v -> {
            if(state.getStatus().equals("playing"))
                pause();

            else if(state.getStatus().equals("paused"))
                resume();

            else
                play();
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
    }

    @Override
    public void onDeviceRefresh(Device device) {

        state = (SpeakerState) device.getState();

        mDevName.setText(device.getParsedName());

        mLocation.setText(getResources().getString(R.string.disp_location,
                device.getRoom().getParsedName(),
                device.getRoom().getHome().getName()));

        mSeekBar.setProgress(state.getVolume());

        mSwitch.setChecked(!state.getStatus().equals("stopped"));

        if(state.getStatus().equals("playing")) {
            mPause.setImageResource(R.drawable.ic_pause);
            enableControlButtons();
        }
        else if(state.getStatus().equals("paused")) {
            mPause.setImageResource(R.drawable.ic_play);
            disableControlButtons();
        }
        else {
            mPause.setImageResource(R.drawable.ic_play);
            disableControlButtons();
        }

        updateGenre(state.getGenre());

        refreshPlaylist();
        Log.v("speaker", String.valueOf(System.currentTimeMillis()));
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

    private void updateGenre(String genre){
        int i;
        //noinspection StatementWithEmptyBody
        for (i = 0; !genres[i].equals(genre); i++);
        mGenre.setSelection(i);
    }

    private void refreshPlaylist(){
        if (state.getStatus().equals(getResources().getString(R.string.off_status_speaker))){

            for (int i = 0; i < 3; i++)
                mPlaylistItems[i].setVisibility(GONE);

            mPlaylist.setVisibility(GONE);

        } else {

            for (int i = 0; i < 3; i++)
                mPlaylistItems[i].setVisibility(VISIBLE);

            mPlaylist.setVisibility(VISIBLE);

            executeAction("getPlaylist", (this::updatePlaylist), this::handleError);
        }
    }

    private void updatePlaylist(Boolean success, Object response){

        if(!success)
            return;

        playlist = parseGetPlaylistResult(response);

        SpeakerState.Song auxSong = ((SpeakerState) device.getValue().getState()).getSong();
        if (auxSong == null) {
            currentSong = -1;
            return;
        }

        int playlistSize = playlist.size();
        String song = auxSong.getTitle();

        int i;
        //noinspection StatementWithEmptyBody
        for (i = 0; i < playlistSize && !playlist.get(i).get("title").toString().equals(song); i++);
        currentSong = (i < playlistSize)? i : 0;

        for (int j = 0; j < 3; j++)
            mPlaylistItems[j].setText(songToString(playlist.get((currentSong + j) % playlistSize)));
    }

    private String songToString(Map<String, Object> song){
        return song.get("artist").toString().concat(" - ").concat(song.get("title").toString());
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