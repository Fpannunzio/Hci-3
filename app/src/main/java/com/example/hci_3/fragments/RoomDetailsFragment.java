package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hci_3.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomDetailsFragment extends Fragment {
    private TextView textView;

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
        textView = view.findViewById(R.id.text_room);
        assert getArguments() != null;
        int amount = 1;//RoomArgs.fromBundle(getArguments()).getNumber();
        textView.setText(String.valueOf(amount));
        if(this.isAdded()){
            textView.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.room_to_homes));
        }

        return view;
    }
}