package com.example.hci_3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_3.R;
import com.example.hci_3.api.Routine;
import com.example.hci_3.view_models.RoutineViewModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> {
    private List<Routine> routines;
    private RoutineViewModel model;

    public RoutineAdapter(RoutineViewModel model){
        routines = new ArrayList<>();
        this.model = model;
    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_view, parent, false);
        return new RoutineViewHolder(view, model, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        Routine routine = routines.get(position);
        holder.setRoutine(routine);
    }

    public void setRoutines(List<Routine> routines){
        this.routines.clear();
        this.routines.addAll(routines);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public static class RoutineViewHolder extends RecyclerView.ViewHolder{
        private TextView mRoutineName;
        private TextView mRoutineDesc;
        private MaterialButton mPlayButton;
        private Routine routine;
        private RoutineViewModel model;
        private Context context;

        public RoutineViewHolder(@NonNull View itemView, RoutineViewModel model, Context context) {
            super(itemView);
            this.model = model;
            this.context = context;
            mRoutineName = itemView.findViewById(R.id.routine_name);
            mRoutineDesc = itemView.findViewById(R.id.routine_desc);
            mPlayButton = itemView.findViewById(R.id.execute_routine);
        }

        public void setRoutine(Routine routine) {

            this.routine = routine;
            mRoutineName.setText(routine.getName());
            mRoutineDesc.setText(routine.getDesc());
            mPlayButton.setOnClickListener(v ->
                model.executeRoutine(this.routine, () -> Toast.makeText(this.context, context.getResources().getString(R.string.rutina_exitosa), Toast.LENGTH_LONG).show(), this::handleError));
        }

        protected void handleError(String message, int code){
            String text = context.getResources().getString(R.string.error_message, message, code);
            Toast.makeText(this.context, text, Toast.LENGTH_LONG).show();
        }
    }

}
