package com.example.hci_3;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.hci_3.api.Device;

public class DoorView extends DeviceView {

    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;
    private TextView mDevName;


    public DoorView(Context context) {
        super(context);
    }

    public DoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.door_view, this, true);

        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);
        mDevName = findViewById(R.id.door_name);

    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);

        extendBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.VISIBLE);
                // Falta rotar la flecha
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDeviceRefresh(Device device) {
        mDevName.setText(getParsedName(device.getName()));
    }

}
