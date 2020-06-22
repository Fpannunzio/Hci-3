package com.example.hci_3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.R;
import com.example.hci_3.api.Room;

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_view, parent,false);
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

        private TextView mRoomName;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoomName = itemView.findViewById(R.id.room_name);
        }

        public void setRoom(Room room) {
            mRoomName.setText(getParsedName(room.getName()));
        }

        private String getParsedName(String fullName){
            String[] aux = fullName.split("_");
            return aux[aux.length - 1];
        }
    }
}
