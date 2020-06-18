package com.example.hci_3;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hci_3.api.Device;

public abstract class DeviceView extends ConstraintLayout {
    protected Device device;

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

    protected abstract void init(Context context);

    public void setDevice(Device device) {
        this.device = device;
    }
}
