package com.example.hci_3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.api.Device;
import com.example.hci_3.api.DeviceTypeInfo;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<MutableLiveData<Device>> devices;

    public DeviceAdapter() {
        devices = new ArrayList<>();
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
        MutableLiveData<Device> ldDevice = devices.get(position);
        holder.setDevice(ldDevice);
    }

    @Override
    public int getItemViewType(int position) {

        @SuppressWarnings("ConstantConditions")
        DeviceTypeInfo e = DeviceTypeInfo.getFromName(devices.get(position).getValue().getTypeName());
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

    public void setDevices(List<MutableLiveData<Device>> devices){
        this.devices.clear();
        this.devices.addAll(devices);
        notifyDataSetChanged();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setDevice(MutableLiveData<Device> ldDevice) {
            ((DeviceView) itemView).setDevice(ldDevice);
        }
    }
}
