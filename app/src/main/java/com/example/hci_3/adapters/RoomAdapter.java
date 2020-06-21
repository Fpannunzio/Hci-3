package com.example.hci_3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.R;
import com.example.hci_3.api.Room;
import com.example.hci_3.device_views.RoomView;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>  {
    private List<MutableLiveData<Room>> rooms;

    public RoomAdapter() {
        rooms = new ArrayList<>();
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_view,null,false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        MutableLiveData<Room> ldRoom = rooms.get(position);
        holder.setRoom(ldRoom);
    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }


    public void setRooms(List<MutableLiveData<Room>> rooms){
            this.rooms.clear();
            this.rooms.addAll(rooms);
            notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setRoom(MutableLiveData<Room> ldRoom) {
            ((RoomView) itemView).setRoom(ldRoom);
        }
    }
}
