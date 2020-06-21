package com.example.hci_3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favoritos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favoritos extends Fragment {

    RecyclerView rv;
    public Favoritos() {
        // Required empty public constructor
    }

    public static Favoritos newInstance(String param1, String param2) {
        Favoritos fragment = new Favoritos();
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