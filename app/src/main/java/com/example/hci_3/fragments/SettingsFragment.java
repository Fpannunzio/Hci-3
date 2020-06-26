package com.example.hci_3.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;

import java.util.Objects;


public class SettingsFragment extends Fragment {

    TextView notificationsText, allNotificationsText, favoriteNotificationsText, defaultNotificationsText, extraSettingsText, themeText;
    Switch allNotificationsSwitch, favoriteNotificationsSwitch, defaultNotificationsSwitch, themeSwitch;
    boolean allNotificationsChecked, favoriteNotificationsChecked, defaultNotificationsChecked, nightModeChecked;
    View view;
    SharedPreferences sharedPreferences;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        setHasOptionsMenu(true);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        Objects.requireNonNull(actionBar).setTitle(R.string.ajustes);

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeButtonEnabled(true);

        sharedPreferences = getContext().getSharedPreferences(getString(R.string.settingsFile), Context.MODE_PRIVATE);

        findViews();
        loadInitialValues();

        return view;
    }

    private void loadInitialValues() {
        notificationsText.setText(getString(R.string.notificationsTitle));
        allNotificationsText.setText(getString(R.string.allNotificationsText));
        favoriteNotificationsText.setText(getString(R.string.favoriteNotificationsText));
        defaultNotificationsText.setText(getString(R.string.defaultNotificationsText));
        extraSettingsText.setText(getString(R.string.extraSettingsText));
        themeText.setText(getString(R.string.themeText));

        allNotificationsChecked = sharedPreferences.getBoolean(String.valueOf(R.id.allNotificationsSwitch),true);
        favoriteNotificationsChecked = sharedPreferences.getBoolean(String.valueOf(R.id.favoriteNotificationsSwitch),true);
        defaultNotificationsChecked = sharedPreferences.getBoolean(String.valueOf(R.id.defaultNotificationsSwitch),true);
        nightModeChecked = sharedPreferences.getBoolean(getString(R.string.night_mode_boolean), false);

        allNotificationsSwitch.setOnCheckedChangeListener(this::handleSwitch);
        allNotificationsSwitch.setChecked(allNotificationsChecked);

        favoriteNotificationsSwitch.setOnCheckedChangeListener(this::handleSwitch);
        favoriteNotificationsSwitch.setChecked(favoriteNotificationsChecked);

        defaultNotificationsSwitch.setOnCheckedChangeListener(this::handleSwitch);
        defaultNotificationsSwitch.setChecked(defaultNotificationsChecked);

        themeSwitch.setOnCheckedChangeListener(this::handleNightModeSwitch);
        themeSwitch.setChecked(nightModeChecked);

        updateSwitches();
    }

    private void findViews() {
        notificationsText =  view.findViewById(R.id.notificationsText);
        allNotificationsText =  view.findViewById(R.id.allNotificationsText);
        favoriteNotificationsText =  view.findViewById(R.id.favoriteNotificationsText);
        defaultNotificationsText =  view.findViewById(R.id.defaultNotificationsText);
        extraSettingsText =  view.findViewById(R.id.extraSettingsText);
        themeText =  view.findViewById(R.id.themeText);

        allNotificationsSwitch = view.findViewById(R.id.allNotificationsSwitch);
        favoriteNotificationsSwitch = view.findViewById(R.id.favoriteNotificationsSwitch);
        defaultNotificationsSwitch = view.findViewById(R.id.defaultNotificationsSwitch);
        themeSwitch = view.findViewById(R.id.themeSwitch);
    }

    private void handleSwitch(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(String.valueOf(compoundButton.getId()),b);
        editor.apply();
        updateSwitches();
    }

    private void handleNightModeSwitch(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (b){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean(getString(R.string.night_mode_boolean), true);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean(getString(R.string.night_mode_boolean), false);
        }
        editor.apply();
    }

    private void updateSwitches(){
        allNotificationsChecked = allNotificationsSwitch.isChecked();
        favoriteNotificationsSwitch.setClickable(allNotificationsChecked);
        defaultNotificationsSwitch.setClickable(allNotificationsChecked);
        favoriteNotificationsSwitch.setEnabled(allNotificationsChecked);
        defaultNotificationsSwitch.setEnabled(allNotificationsChecked);
        favoriteNotificationsText.setEnabled(allNotificationsChecked);
        defaultNotificationsText.setEnabled(allNotificationsChecked);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_settings, menu);
    }


}