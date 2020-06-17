package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.hci_3.api.Device;

public class DeviceView extends LinearLayout { //ACView. Queda como DeviceView para pruebas
    public DeviceView(Context context) {
        super(context);
        init(context);
    }

    public DeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.ac_view, this, true);

        // Aca guardo los elementos de mi view
    }

    @Override // Equivalente a Mounted
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setDevice(Device device) {
        // Aca se cargan los parametros del device


        // mName.setText(contact.getName());
    }
}
