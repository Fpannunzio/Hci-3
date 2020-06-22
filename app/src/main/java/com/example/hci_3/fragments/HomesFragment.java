package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;
import com.example.hci_3.adapters.RoomAdapter;
import com.example.hci_3.api.Home;
import com.example.hci_3.api.Room;
import com.example.hci_3.view_models.FavoriteViewModel;
import com.example.hci_3.view_models.HomesViewModel;


import java.util.ArrayList;
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


    public HomesFragment() {
        // Required empty public constructor
    }

    public static HomesFragment newInstance(String param1, String param2) {
        HomesFragment fragment = new HomesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homes, container, false);

        adapter = new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item);

        spinner = view.findViewById(R.id.homes_spinner);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                Home home = homes.getValue().get(arg2);
                model.updateCurrentHome(home);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        recyclerAdapter = new RoomAdapter();

        recyclerAdapter.setOnClickListener(v -> Navigation.findNavController(v).navigate(HomesFragmentDirections.homesToRoom(recyclerAdapter.getRoomId())));

        rv = view.findViewById(R.id.room_recycler);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        rv.setAdapter(recyclerAdapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));

        return view;
    }

    private void refreshHomes(List<Home> homes){
        adapter.clear();
        adapter.addAll(homes.stream().map(Home::getName).collect(Collectors.toList()));
        adapter.notifyDataSetChanged();
    }

    private void refreshRooms(List<Room> rooms){
        recyclerAdapter.setRooms(rooms);
    }
}