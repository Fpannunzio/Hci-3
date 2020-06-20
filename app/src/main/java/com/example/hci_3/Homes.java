package com.example.hci_3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static androidx.navigation.Navigation.findNavController;

public class Homes extends Fragment {

    private TextView textView;
    public Homes() {
        // Required empty public constructor
    }

    public static Homes newInstance(String param1, String param2) {
        Homes fragment = new Homes();
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
        View view = inflater.inflate(R.layout.fragment_homes, container, false);
        textView = view.findViewById(R.id.text_home);
        if(this.isAdded()){
            textView.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.homes_to_room));
        }

        return view;
    }
}