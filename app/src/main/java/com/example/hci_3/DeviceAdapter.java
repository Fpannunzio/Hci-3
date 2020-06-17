package com.example.hci_3;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.api.Device;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private enum dispTypes{
        AC(1), BLIND(2), FAUCET(3), OVEN(4),
        VACUUM(5), DOOR(6), SPEAKER(7), LAMP(8);

        private int numVal;

        dispTypes(int numVal){
            this.numVal = numVal;
        }
        public int getNumVal() {
            return numVal;
        }
    }

    private List<Device> devices;

    public DeviceAdapter(List<Device> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("message1","onCreateViewHolder");
        DeviceView v = getView(parent, viewType);
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
        switch (devices.get(position).getTypeName()) {
           case "ac": return dispTypes.AC.getNumVal();
           case "blinds": return dispTypes.BLIND.getNumVal();
           case "faucet": return dispTypes.FAUCET.getNumVal();
           case "oven": return dispTypes.OVEN.getNumVal();
           case "vacuum": return dispTypes.VACUUM.getNumVal();
           case "door": return dispTypes.DOOR.getNumVal();
           case "speaker": return dispTypes.SPEAKER.getNumVal();
           case "lamp": return dispTypes.LAMP.getNumVal();
           default: throw new RuntimeException("Invalid or unsupported type");
       }

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

    private DeviceView getView(@NonNull ViewGroup parent, int viewType ){
        Log.v("message3","onCreateViewHolder");
        return new ACView(parent.getContext());
        /*switch(viewType) {
            case 1:
                return new ACView(parent.getContext());
            case 2:
                return null;
            default:
                throw new RuntimeException("Invalid or unsupported device");
        }*/
    }
}
