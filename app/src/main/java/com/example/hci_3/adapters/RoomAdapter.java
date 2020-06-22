package com.example.hci_3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.R;
import com.example.hci_3.api.Room;
import com.example.hci_3.device_views.RoomView;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder>  {
    private List<Room> rooms;

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
        Room room = rooms.get(position);
        holder.setRoom(room);
    }


    @Override
    public int getItemCount() {
        return rooms.size();
    }


    public void setRooms(List<Room> rooms){
            this.rooms.clear();
            this.rooms.addAll(rooms);
            notifyDataSetChanged();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setRoom(Room room) {
            ((RoomView) itemView).setRoom(room);
        }
    }
}
