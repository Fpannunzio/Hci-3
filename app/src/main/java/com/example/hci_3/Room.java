package com.example.hci_3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Room#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Room extends Fragment {
    private TextView textView;

    public Room() {
        // Required empty public constructor
    }

    public static Room newInstance(String param1, String param2) {
        Room fragment = new Room();
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
        int amount = RoomArgs.fromBundle(getArguments()).getNumber();
        textView.setText(String.valueOf(amount));
        if(this.isAdded()){
            textView.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.room_to_homes));
        }

        return view;
    }
}