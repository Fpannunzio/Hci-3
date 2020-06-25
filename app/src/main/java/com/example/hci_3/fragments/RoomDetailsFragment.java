package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;
import com.example.hci_3.adapters.DeviceAdapter;
import com.example.hci_3.view_models.RoomDetailsViewModel;

import java.util.Objects;

public class RoomDetailsFragment extends Fragment {

    RecyclerView rv;
    RoomDetailsViewModel model;
    DeviceAdapter adapter;


    public RoomDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(RoomDetailsViewModel.class);

        String roomId = RoomDetailsFragmentArgs.fromBundle(requireArguments()).getRoomId();

        model.setRoom(roomId);

        adapter = new DeviceAdapter(model);

        model.getDevices().observe(requireActivity(), adapter::setDevices);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_room, container, false);
        setHasOptionsMenu(true);

        rv = view.findViewById(R.id.room_recycler);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        rv.setAdapter(adapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        String roomName = RoomDetailsFragmentArgs.fromBundle(requireArguments()).getRoomName();
        String homeName = RoomDetailsFragmentArgs.fromBundle(requireArguments()).getHomeName();

        Objects.requireNonNull(actionBar).setTitle(getResources().getString(R.string.room_title, roomName, homeName));

        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setHomeButtonEnabled(true);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem settingsItem = menu.findItem(R.id.settings);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(RoomDetailsFragmentDirections.actionRoomToSearchFragment(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        settingsItem.setOnMenuItemClickListener(item -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(RoomDetailsFragmentDirections.actionRoomToSettingsFragment());
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(requireActivity(), R.id.nav_host_fragment) ) ||
        super.onOptionsItemSelected(item);
    }
}