package com.example.hci_3;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.api.Device;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<Device> devices;

    public DeviceAdapter(List<Device> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceView v = new DeviceView(parent.getContext());
        return new DeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        Device device = devices.get(position);
        holder.setDevice(device);
    }

    @Override
    public int getItemCount() {
        return devices.size();
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
