package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hci_3.R;
import com.example.hci_3.SpacesItemDecoration;
import com.example.hci_3.adapters.DeviceAdapter;
import com.example.hci_3.view_models.FavoriteViewModel;
import com.example.hci_3.view_models.RoomDetailsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomDetailsFragment extends Fragment {
    RecyclerView rv;

    public RoomDetailsFragment() {
        // Required empty public constructor
    }

    public static RoomDetailsFragment newInstance(String param1, String param2) {
        RoomDetailsFragment fragment = new RoomDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        assert getArguments() != null;
        String roomId = RoomDetailsFragmentArgs.fromBundle(getArguments()).getRoomId();
        RoomDetailsViewModel model = new RoomDetailsViewModel(roomId);

        DeviceAdapter adapter = new DeviceAdapter();

        rv = view.findViewById(R.id.room_recycler);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        rv.setAdapter(adapter);

        rv.addItemDecoration(new SpacesItemDecoration(30));

        if(getActivity() != null)
            model.getDevices().observe(getActivity(), adapter::setDevices);

        else
            throw new RuntimeException("fragment is null");


        return view;
    }
}