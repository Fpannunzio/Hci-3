package com.example.hci_3.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;
import com.example.hci_3.adapters.RoomAdapter;
import com.example.hci_3.api.Home;
import com.example.hci_3.api.Room;
import com.example.hci_3.view_models.HomesViewModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class HomesFragment extends Fragment {

    RecyclerView rv;
    HomesViewModel model;
    LiveData<List<Home>> homes;
    LiveData<List<Room>> rooms;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    RoomAdapter recyclerAdapter;
    SharedPreferences sharedPreferences;
    String spinnerValue = null;


    public HomesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(HomesViewModel.class);

        model.startUpdatingHomes();

        homes = model.getHomes();

        homes.observe(this, this::refreshHomes);

        rooms = model.getRooms();

        rooms.observe(this, this::refreshRooms);

        sharedPreferences = requireContext().getSharedPreferences("spinnerSP", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homes, container, false);

        setHasOptionsMenu(true);

        adapter = new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item);

        spinner = view.findViewById(R.id.homes_spinner);

        spinner.setAdapter(adapter);

        setHasOptionsMenu(true);

        spinnerValue = sharedPreferences.getString("home_spinner_value", null);

        if(spinnerValue != null && adapter.getPosition(spinnerValue) != -1)
            spinner.setSelection(adapter.getPosition(spinnerValue));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                Home home = Objects.requireNonNull(homes.getValue()).get(arg2);
                model.updateCurrentHome(home);

                //noinspection ConstantConditions
                updateSpinnerValue(adapter.getItem(arg2).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        recyclerAdapter = new RoomAdapter();

        rv = view.findViewById(R.id.room_recycler);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        rv.setAdapter(recyclerAdapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        Objects.requireNonNull(actionBar).setTitle(R.string.hogares);

        actionBar.setDisplayHomeAsUpEnabled(false);

        actionBar.setHomeButtonEnabled(false);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem settingsItem = menu.findItem(R.id.settings);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(HomesFragmentDirections.actionHogaresToSearchFragment(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        settingsItem.setOnMenuItemClickListener(item -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(HomesFragmentDirections.actionHogaresToSettingsFragment());
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        model.startUpdatingHomes();
        model.startUpdatingRooms();
    }

    @Override
    public void onPause() {
        super.onPause();
        model.stopUpdatingHomes();
    }

    private void refreshHomes(List<Home> homes){

        adapter.clear();
        adapter.addAll(homes.stream().map(Home::getName).collect(Collectors.toList()));
        adapter.notifyDataSetChanged();

        if(adapter.getCount() == 0){
            // Que mostramos si no hay casas??
            updateSpinnerValue(null);
            return;
        }

        int newPosition = -1;
        if(spinnerValue != null)
            newPosition = adapter.getPosition(spinnerValue);

        if(newPosition == -1) {
            newPosition = 0;
            if(spinnerValue != null)
                Toast.makeText(requireContext(), getResources().getString(R.string.spinner_invalid_home_name), Toast.LENGTH_LONG).show();
        }

        //noinspection ConstantConditions
        updateSpinnerValue(adapter.getItem(newPosition).toString());
        spinner.setSelection(newPosition);
    }

    private void refreshRooms(List<Room> rooms){
        recyclerAdapter.setRooms(rooms);
    }

    private void updateSpinnerValue(String value){

        if((value == null && spinnerValue != null) || (value != null && !value.equals(spinnerValue))) {
            spinnerValue = value;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("home_spinner_value", spinnerValue);
            editor.apply();
        }
    }
}