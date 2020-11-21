package com.pi.gymapp.ui.routine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.api.ApiKeywords;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.utils.StringUtils;

import java.util.List;

public class RoutinesListAdapter extends RecyclerView.Adapter<RoutinesListAdapter.ViewHolder> {

    private final List<Routine> data;

    public RoutinesListAdapter(List<Routine> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.routines_list_card, parent, false);

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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int id;

        public TextView name, rate, difficulty;

        public ViewHolder(@NonNull View view) {
            super(view);

            name = itemView.findViewById(R.id.routineNameChip);
            rate = itemView.findViewById(R.id.routineRateChip);
            difficulty = itemView.findViewById(R.id.routineDifficultyChip);

            view.setOnClickListener(this);
        }

        public void bindTo(Routine routine) {
            id = routine.getId();

            name.setText(routine.getName());
            difficulty.setText(ApiKeywords.getDifficulty(difficulty.getContext(), routine.getDifficulty()));

            Context context = name.getContext();
            rate.setText(String.format(context.getString(R.string.rateFormat), routine.getRate()));
        }

        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(
                RoutinesExploreFragmentDirections.actionNavHomeToRoutineDetailFragment(id)
            );
        }
    }

}
