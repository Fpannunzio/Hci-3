package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hci_3.R;
import com.example.hci_3.api.Device;

public abstract class DeviceView extends ConstraintLayout {
    protected Device device;

    public DeviceView(Context context) {
        super(context);

    }
    public DeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public DeviceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    protected void init(Context context) {
        throw new ExceptionInInitializerError();
    }

    public void setDevice(Device device) {
        this.device = device;

    }
}
