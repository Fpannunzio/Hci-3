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

import com.example.hci_3.adapters.DeviceAdapter;
import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;

import com.example.hci_3.view_models.FavoriteViewModel;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    RecyclerView rv;
    FavoriteViewModel model;
    DeviceAdapter adapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(FavoriteViewModel.class);

        adapter = new DeviceAdapter(model);

        model.getDevices().observe(requireActivity(), adapter::setDevices);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        setHasOptionsMenu(true);

        rv = view.findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        rv.setAdapter(adapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();

        Objects.requireNonNull(actionBar).setTitle(R.string.favoritos);

        actionBar.setDisplayHomeAsUpEnabled(false);

        actionBar.setHomeButtonEnabled(false);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.

            MenuItem searchItem = menu.findItem(R.id.action_search);

            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(FavoritesFragmentDirections.actionFavoritosToSearchFragment(query));
                    return true;

                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

    @Override
    public void onResume() {
        super.onResume();
        model.continuePollingStates();
    }

    @Override
    public void onPause() {
        super.onPause();
        model.pausePollingStates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.stopPollingStates();
    }
}