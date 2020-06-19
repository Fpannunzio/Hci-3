package com.example.hci_3;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hci_3.api.Device;

public class VacuumView extends DeviceView {

    private TextView mDevName;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private Spinner locationSpinner;
    private ArrayAdapter<CharSequence> locationAdapter;

    public VacuumView(Context context) {
        super(context);
    }

    public VacuumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VacuumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.vacuum_view, this, true);

        mDevName = findViewById(R.id.ac_name);
        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);
        locationSpinner = findViewById(R.id.locationSpinner);
        locationAdapter = ArrayAdapter.createFromResource(context, R.array.vacuum_location, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(Device device) {
        super.setDevice(device);
        locationSpinner.setAdapter(locationAdapter);

        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableLayout.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableLayout.setVisibility(View.VISIBLE);
                    // Falta rotar la flecha
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableLayout.setVisibility(View.GONE);
                }
            }
        });
    }
}