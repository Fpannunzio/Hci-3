package com.example.hci_3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceTypeInfo;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> devices;

    public DeviceAdapter(List<Device> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        DeviceView v = getView(parent.getContext(), viewType);
        if(v == null)
            throw new RuntimeException("Invalid device");

        return new DeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device device = devices.get(position);
        holder.setDevice(device);
    }

    @Override
    public int getItemViewType(int position) {

        DeviceTypeInfo e = DeviceTypeInfo.getFromName(devices.get(position).getTypeName());
        if(e == null)
            throw new RuntimeException("Invalid or unsupported DeviceType");

        return e.ordinal();
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    private DeviceView getView(Context context, int viewType){

        return DeviceTypeInfo.values()[viewType].getView(context);
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setDevice(Device device) {
            ((DeviceView) itemView).setDevice(device);
        }
    }
}
