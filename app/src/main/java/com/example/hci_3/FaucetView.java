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

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hci_3.api.Device;

public class FaucetView extends DeviceView {

    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;

    private Spinner unitSpinner;
    private ArrayAdapter<CharSequence> unitAdapter;

    public FaucetView(Context context) {
        super(context);
    }

    public FaucetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FaucetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.faucet_view, this, true);

        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        unitSpinner = findViewById(R.id.unitSpinner);
        unitAdapter = ArrayAdapter.createFromResource(context, R.array.faucet_units, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(Device device) {
        super.setDevice(device);
        unitSpinner.setAdapter(unitAdapter);

        extendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableLayout.getVisibility() == View.GONE){
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