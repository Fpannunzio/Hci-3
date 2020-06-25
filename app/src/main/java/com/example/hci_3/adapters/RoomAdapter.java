package com.example.hci_3.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.R;
import com.example.hci_3.api.Room;
import com.example.hci_3.fragments.HomesFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
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


    public static class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mRoomName;
        private String roomId, roomName, homeName;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoomName = itemView.findViewById(R.id.room_name);
            CardView cardView = itemView.findViewById(R.id.room_card);

            cardView.setOnClickListener(this);
        }

        public void setRoom(Room room) {
            roomId = room.getId();
            roomName = room.getParsedName();
            if(room.getHome() != null)
                Log.v("homename", room.getHome().getName());
            mRoomName.setText(roomName);
        }

        public String getRoomId() {
            return roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public String getHomeNameName() {
            return homeName;
        }

        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(HomesFragmentDirections.homesToRoom(getRoomId(), getRoomName(),getHomeNameName()));
        }
    }
}
