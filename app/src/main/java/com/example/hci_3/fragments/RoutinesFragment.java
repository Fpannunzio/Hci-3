package com.example.hci_3.fragments;

import android.os.Bundle;

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
import com.example.hci_3.repositories.DeviceRepository;
import com.example.hci_3.view_models.FavoriteDeviceViewModel;


public class RoutinesFragment extends Fragment {

    RecyclerView rv;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rutinas, container, false);

        FavoriteDeviceViewModel model = new ViewModelProvider(this).get(FavoriteDeviceViewModel.class);

        DeviceAdapter adapter = new DeviceAdapter();

        rv = view.findViewById(R.id.recyclerView);
        if(this.isAdded()){
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else
            throw new RuntimeException("fragment is null");
        rv.setAdapter(adapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));
        if(getActivity() != null){
            model.getDevices().observe(getActivity(), adapter::setDevices);
        }
        else
            throw new RuntimeException("fragment is null");

        DeviceRepository.getInstance().getDevices();

        return view;
    }

}
