package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.hci_3.view_models.FavoriteViewModel;
import com.example.hci_3.view_models.HomesViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomesFragment extends Fragment {

    RecyclerView rv;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homes, container, false);

        HomesViewModel model = new ViewModelProvider(this).get(HomesViewModel.class);

        LiveData<List<Home>> Homes = model.getHomes();

        Spinner spinner = view.findViewById(R.id.homes_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, Objects.requireNonNull(Homes.getValue()));

        spinner.setAdapter(adapter);
        RoomAdapter recyclerAdapter = new RoomAdapter();

        rv = view.findViewById(R.id.room_recycler);
        if(this.isAdded()){
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else
            throw new RuntimeException("fragment is null");
        rv.setAdapter(recyclerAdapter);
        rv.addItemDecoration(new SpacesItemDecoration(30));

        if(getActivity() != null){
            //model.getRooms().observe(getActivity(), adapter::setRooms);
        }
        else
            throw new RuntimeException("fragment is null");


        /*textView.setOnClickListener(v ->
                    Navigation.findNavController(view).navigate(HomesFragmentDirections.homesToRoom().setNumber(22)));*/

        return view;
    }
}