package com.example.hci_3.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hci_3.R;

import static androidx.navigation.Navigation.findNavController;

public class HomesFragment extends Fragment {

    private TextView textView;
    public HomesFragment() {
        // Required empty public constructor
    }

    public static HomesFragment newInstance(String param1, String param2) {
        HomesFragment fragment = new HomesFragment();
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
            textView.setOnClickListener(v ->
                    Log.v("", ""));
                    //Navigation.findNavController(view).navigate(HomesDirections.homesToRoom().setNumber(22)));
        }

        return view;
    }
}