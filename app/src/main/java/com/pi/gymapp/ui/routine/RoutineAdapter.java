package com.pi.gymapp.ui.routine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.domain.Routine;

import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    private final List<Routine> data;

    public RoutineAdapter(List<Routine> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.routine_card, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Routine routine = data.get(position);
        holder.bindTo(routine);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private int id;

        public TextView title, rate, duration;

        public ViewHolder(@NonNull View view) {
            super(view);

            title = itemView.findViewById(R.id.routineName);
            rate = itemView.findViewById(R.id.routineRating);
            duration = itemView.findViewById(R.id.routineDuration);
        }

        public void bindTo(Routine routine) {
            id = routine.getId();

            title.setText(routine.getTitle());

            Context context = title.getContext();
            rate.setText(String.format(context.getString(R.string.rateFormat), routine.getRate()));
        }

    }

}
