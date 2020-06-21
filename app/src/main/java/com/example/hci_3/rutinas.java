package com.example.hci_3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;

import java.util.ArrayList;
import java.util.List;


public class rutinas extends Fragment {

    RecyclerView rv;
    List<Device> devices;

    public rutinas() {
        // Required empty public constructor
    }


    public static rutinas newInstance(String param1, String param2) {
        rutinas fragment = new rutinas();
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
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

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
        model.getDevices().observe(getActivity(), adapter::setDevices);

        DeviceRepository.getInstance().getDevices();

        /*ApiClient.getInstance().getDevices((devices) -> {

            this.devices.addAll(devices);
            adapter.notifyDataSetChanged();

        }, this::handleError);*/

        return view;
    }
    private void handleError(String message, int code){
        String text = getResources().getString(R.string.error_message, message, code);

        if(this.isAdded()){
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        }
        else
            throw new RuntimeException("fragment in toast is null");

    }
}