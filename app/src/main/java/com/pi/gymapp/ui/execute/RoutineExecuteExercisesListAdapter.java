package com.pi.gymapp.ui.execute;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.domain.Exercise;

import java.util.List;

public class RoutineExecuteExercisesListAdapter extends RecyclerView.Adapter<RoutineExecuteExercisesListAdapter.ViewHolder> {

    private final List<Exercise> data;

    public RoutineExecuteExercisesListAdapter(List<Exercise> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public  RoutineExecuteExercisesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.exercise_list_card, parent, false);

        return new  RoutineExecuteExercisesListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull  RoutineExecuteExercisesListAdapter.ViewHolder holder, int position) {
        Exercise exercise = data.get(position);
        holder.bindTo(exercise);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private int cycleId;
        private int routineId;

        public TextView title, repetitions, duration;

        public ViewHolder(@NonNull View view) {
            super(view);

            title = itemView.findViewById(R.id.exerciseTitle);
            repetitions = itemView.findViewById(R.id.exerciseRepetitions);
            duration = itemView.findViewById(R.id.exerciseDuration);

        }

        public void bindTo(Exercise exercise) {
            id = exercise.getId();
            cycleId=exercise.getCycleId();
            routineId=exercise.getRoutineId();

            title.setText(exercise.getName());
            duration.setText(Integer.toString(exercise.getDuration()));
//            Context context = title.getContext();
            repetitions.setText(Integer.toString(exercise.getRepetitions()));
        }


    }

}
