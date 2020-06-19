package com.example.hci_3;

import android.content.Context;
import android.os.Bundle;
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

import com.example.hci_3.DeviceView;
import com.example.hci_3.R;
import com.example.hci_3.api.Device;

public class OvenView extends DeviceView {
    CardView cardView;
    ConstraintLayout expandableLayout;
    ImageButton extendBtn;

    TextView fuenteCalor;
    Spinner fuenteSpinner;

    TextView modoGrill;
    Spinner grillSpinner;

    TextView modoConveccion;
    Spinner conveccionSpinner;

    ArrayAdapter<CharSequence> fuenteAdapter;
    ArrayAdapter<CharSequence> grillAdapter;
    ArrayAdapter<CharSequence> conveccionAdapter;

    public OvenView(Context context) {
        super(context);
    }

    public OvenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OvenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.oven_view, this, true);

        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);

        fuenteCalor = findViewById(R.id.fuenteCalor);
        fuenteSpinner = findViewById(R.id.fuenteSpinner);

        modoGrill = findViewById(R.id.modoGrill);
        grillSpinner = findViewById(R.id.grillSpinner);

        modoConveccion = findViewById(R.id.modoConveccion);
        conveccionSpinner = findViewById(R.id.conveccionSpinner);

        fuenteAdapter = ArrayAdapter.createFromResource(context, R.array.fuente_calor, android.R.layout.simple_spinner_item);
        grillAdapter = ArrayAdapter.createFromResource(context, R.array.modo_grill, android.R.layout.simple_spinner_item);
        conveccionAdapter = ArrayAdapter.createFromResource(context, R.array.modo_conveccion, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(Device device) {
        super.setDevice(device);
        fuenteSpinner.setAdapter(fuenteAdapter);
        grillSpinner.setAdapter(grillAdapter);
        conveccionSpinner.setAdapter(conveccionAdapter);

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
