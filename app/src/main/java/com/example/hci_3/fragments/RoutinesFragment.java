package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
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

import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;
import com.example.hci_3.adapters.RoutineAdapter;
import com.example.hci_3.api.Routine;
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem settingsItem = menu.findItem(R.id.settings);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(RoutinesFragmentDirections.actionRutinasToSearchFragment(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        settingsItem.setOnMenuItemClickListener(item -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(RoutinesFragmentDirections.actionRutinasToSettingsFragment());
            return false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        model.startPolling();
    }

    @Override
    public void onPause() {
        super.onPause();
        model.stopPolling();
    }

    private void refreshRoutines(List<Routine> routines) {
        adapter.setRoutines(routines);
    }

}
