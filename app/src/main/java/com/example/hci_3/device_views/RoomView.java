package com.example.hci_3.device_views;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.hci_3.R;
import com.example.hci_3.api.Room;

public class RoomView  extends ConstraintLayout {
    protected LiveData<Room> room;
    protected Context context;
    private TextView mRoomName;

    public RoomView(Context context) {
        super(context);
        init(context);
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context){
        this.context = context;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        LayoutInflater.from(context).inflate(R.layout.room_view, this, true);

        mRoomName = findViewById(R.id.room_name);
    }

    public void setRoom(LiveData<Room> room) {
        this.room = room;
        this.room.observe(getLifecycleOwner(), this::onRoomRefresh);
    }


    public void onRoomRefresh(Room room){
        mRoomName.setText(getParsedName(room.getName()));
    }
    protected String getParsedName(String fullName){
        String[] aux = fullName.split("_");
        // Aca se cargan los parametros del device
        return aux[aux.length - 1];
    }
    protected LifecycleOwner getLifecycleOwner() {
        Context context = getContext();
        while (!(context instanceof LifecycleOwner)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (LifecycleOwner) context;
    }
}
