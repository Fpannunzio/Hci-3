package com.example.hci_3;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;

import com.example.hci_3.api.ApiClient;
import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceStates.OvenState;

import java.util.ArrayList;
import java.util.Collections;

public class OvenView extends DeviceView {
    private TextView mDevName, mState, mTemperature, mLocation;
    private Switch mSwitch;
    private ImageButton mMinus, mPlus;
    private Spinner mFont, mGrill, mConvection;
    private CardView cardView;
    private ConstraintLayout expandableLayout;
    private ImageButton extendBtn;


    private ArrayAdapter<CharSequence> fuenteAdapter;
    private ArrayAdapter<CharSequence> grillAdapter;
    private ArrayAdapter<CharSequence> conveccionAdapter;

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
        super.init(context);
        LayoutInflater.from(context).inflate(R.layout.oven_view, this, true);

        mLocation = findViewById(R.id.oven_location);
        mDevName = findViewById(R.id.oven_name);
        mTemperature = findViewById(R.id.oven_temp);
        mSwitch = findViewById(R.id.oven_switch);
        mState = findViewById(R.id.oven_state);
        mMinus = findViewById(R.id.oven_minus);
        mPlus = findViewById(R.id.oven_plus);

        cardView = findViewById(R.id.cardView);
        expandableLayout = findViewById(R.id.expandableLayout);
        extendBtn = findViewById(R.id.expandBtn);


        mFont = findViewById(R.id.oven_fuente);
        mGrill = findViewById(R.id.oven_grill);
        mConvection = findViewById(R.id.oven_conveccion);

        fuenteAdapter = ArrayAdapter.createFromResource(context, R.array.fuente_calor, android.R.layout.simple_spinner_item);
        grillAdapter = ArrayAdapter.createFromResource(context, R.array.modo_grill, android.R.layout.simple_spinner_item);
        conveccionAdapter = ArrayAdapter.createFromResource(context, R.array.modo_conveccion, android.R.layout.simple_spinner_item);
    }

    @Override
    public void setDevice(LiveData<Device> device) {
        super.setDevice(device);
        mDevName.setText(getParsedName(device.getName()));

        mLocation.setText(getResources().getString(R.string.disp_location, getParsedName(device.getRoom().getName()), device.getRoom().getHome().getName()));
        mState.setText(getResources().getString(R.string.temp_state, ((OvenState) device.getState()).getStatus().equals("on") ? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado), ((OvenState) device.getState()).getTemperature()));
        mTemperature.setText(getResources().getString(R.string.temp, String.valueOf(((OvenState) device.getState()).getTemperature())));
        mFont.setAdapter(fuenteAdapter);
        mGrill.setAdapter(grillAdapter);
        mConvection.setAdapter(conveccionAdapter);

        final String[] fuenteArray = getResources().getStringArray(R.array.fuente_calor);
        final String[]  grillArray= getResources().getStringArray(R.array.modo_grill);
        final String[] convectionArray = getResources().getStringArray(R.array.modo_conveccion);

        mFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setFont(fuenteArray[arg2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mGrill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setGrill(grillArray[arg2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mConvection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setConvection(convectionArray[arg2]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        extendBtn.setOnClickListener(v -> {
            if (expandableLayout.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.VISIBLE);
                // Falta rotar la flecha
            } else {
                TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                expandableLayout.setVisibility(View.GONE);
            }
        });
        mMinus.setOnClickListener(v -> {
            int temp = ((OvenState) device.getState()).getTemperature() - 5;
            if (temp >= 90)
                ApiClient.getInstance().executeAction(device.getId(), "setTemperature", new ArrayList<>(Collections.singletonList(temp)), (success) -> {
                    if (success) {
                        ((OvenState) device.getState()).setTemperature(temp);
                        mTemperature.setText(getResources().getString(R.string.temp, String.valueOf(temp)));
                        mState.setText(getResources().getString(R.string.temp_state, ((OvenState) device.getState()).getStatus().equals("on") ? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado), ((OvenState) device.getState()).getTemperature()));
                    }
                }, this::handleError);
            else
                Toast.makeText(context, getResources().getString(R.string.invalid_temp), Toast.LENGTH_LONG).show();
        });
        mPlus.setOnClickListener(v -> {
            int temp = ((OvenState) device.getState()).getTemperature() + 5;
            if (temp <= 230)
                ApiClient.getInstance().executeAction(device.getId(), "setTemperature", new ArrayList<>(Collections.singletonList(temp)), (success) -> {
                    if (success) {
                        ((OvenState) device.getState()).setTemperature(temp);
                        mTemperature.setText(getResources().getString(R.string.temp, String.valueOf(temp)));
                        mState.setText(getResources().getString(R.string.temp_state,
                                ((OvenState) device.getState()).getStatus().equals("on") ? getResources().getString(R.string.prendido) : getResources().getString(R.string.apagado),
                                ((OvenState) device.getState()).getTemperature()));
                    }
                }, this::handleError);
            else
                Toast.makeText(context, getResources().getString(R.string.invalid_temp), Toast.LENGTH_LONG).show();

        });

        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                ApiClient.getInstance().executeAction(device.getId(), "turnOn", new ArrayList<>(), (success) -> {
                    if (success)
                        ((OvenState) device.getState()).setStatus("on");
                    mState.setText(getResources().getString(R.string.temp_state, getResources().getString(R.string.prendido),
                            ((OvenState) device.getState()).getTemperature()));
                }, this::handleError);
            else
                ApiClient.getInstance().executeAction(device.getId(), "turnOff", new ArrayList<>(), (success) -> {
                    if (success)
                        ((OvenState) device.getState()).setStatus("off");
                    mState.setText(getResources().getString(R.string.temp_state, getResources().getString(R.string.apagado),
                            ((OvenState) device.getState()).getTemperature()));
                }, this::handleError);
        });
    }

    private void setFont(String value) {
        switch (value) {
            case "Convencional": {
                ApiClient.getInstance().executeAction(device.getId(), "setHeat", new ArrayList<>(Collections.singletonList("conventional")),
                        (success) -> ((OvenState) device.getState()).setHeat("conventional"),
                        this::handleError);
                break;
            }
            case "Abajo": {
                ApiClient.getInstance().executeAction(device.getId(), "setHeat", new ArrayList<>(Collections.singletonList("bottom")),
                        (success) -> ((OvenState) device.getState()).setHeat("bottom"),
                        this::handleError);
                break;
            }
            case "Arriba": {
                ApiClient.getInstance().executeAction(device.getId(), "setHeat", new ArrayList<>(Collections.singletonList("top")),
                        (success) -> ((OvenState) device.getState()).setHeat("top"),
                        this::handleError);
                break;
            }
        }
    }

    private void setGrill(String value) {
        switch (value) {
            case "Apagado": {
                ApiClient.getInstance().executeAction(device.getId(), "setGrill", new ArrayList<>(Collections.singletonList("off")),
                        (success) -> ((OvenState) device.getState()).setGrill("off"),
                        this::handleError);
                break;
            }
            case "Economico": {
                ApiClient.getInstance().executeAction(device.getId(), "setGrill", new ArrayList<>(Collections.singletonList("eco")),
                        (success) -> ((OvenState) device.getState()).setGrill("eco"),
                        this::handleError);
                break;
            }
            case "Completo": {
                ApiClient.getInstance().executeAction(device.getId(), "setGrill", new ArrayList<>(Collections.singletonList("large")),
                        (success) -> ((OvenState) device.getState()).setGrill("large"),
                        this::handleError);
                break;
            }
        }
    }

    private void setConvection(String value) {
        switch (value) {
            case "Convencional": {
                ApiClient.getInstance().executeAction(device.getId(), "setConvection", new ArrayList<>(Collections.singletonList("normal")),
                        (success) -> ((OvenState) device.getState()).setConvection("normal"),
                        this::handleError);
                break;
            }
            case "Apagado": {
                ApiClient.getInstance().executeAction(device.getId(), "setConvection", new ArrayList<>(Collections.singletonList("off")),
                        (success) -> ((OvenState) device.getState()).setConvection("off"),
                        this::handleError);
                break;
            }
            case "Economico": {
                ApiClient.getInstance().executeAction(device.getId(), "setConvection", new ArrayList<>(Collections.singletonList("eco")),
                        (success) -> ((OvenState) device.getState()).setConvection("eco"),
                        this::handleError);
                break;
            }
        }
    }

    @Override
    public void onDeviceRefresh(Device device) {
        Log.v("deviceStateChange", "OVEN");
    }
}
