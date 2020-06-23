package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hci_3.adapters.DeviceAdapter;
import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;
import com.example.hci_3.adapters.RoutineAdapter;
import com.example.hci_3.api.Routine;
import com.example.hci_3.repositories.DeviceRepository;
import com.example.hci_3.view_models.FavoriteViewModel;
import com.example.hci_3.view_models.RoutineViewModel;

import java.util.List;

import java.util.Objects;


public class RoutinesFragment extends Fragment {

    RecyclerView rv;
    RoutineViewModel model;
    RoutineAdapter adapter;

    public RoutinesFragment() {
        // Required empty public constructor
    }

    public static RoutinesFragment newInstance(String param1, String param2) {
        RoutinesFragment fragment = new RoutinesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(RoutineViewModel.class);

        model.startPolling();

        model.getRoutines().observe(this, this::refreshRoutines);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rutinas, container, false);

        adapter = new RoutineAdapter(model);

        rv = view.findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        rv.setAdapter(adapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        Objects.requireNonNull(actionBar).setTitle(R.string.rutinas);

        actionBar.setDisplayHomeAsUpEnabled(false);

        actionBar.setHomeButtonEnabled(false);

        return view;
    }

    private void refreshRoutines(List<Routine> routines) {
        adapter.setRoutines(routines);
    }

}
