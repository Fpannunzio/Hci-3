package com.example.hci_3.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        FavoriteViewModel model = new ViewModelProvider(this).get(FavoriteViewModel.class);

        DeviceAdapter adapter = new DeviceAdapter();

        rv = view.findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        rv.setAdapter(adapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));

        model.getDevices().observe(requireActivity(), adapter::setDevices);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(R.string.favoritos);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

        return view;
    }
}