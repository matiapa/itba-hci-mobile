package com.pi.gymapp.ui.exercise;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.domain.Exercise;

import java.util.List;

public class ExercisesListAdapter extends RecyclerView.Adapter<ExercisesListAdapter.ViewHolder> {

    private final List<Exercise> data;

    public ExercisesListAdapter(List<Exercise> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public  ExercisesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.exercise_list_card, parent, false);

        return new  ExercisesListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull  ExercisesListAdapter.ViewHolder holder, int position) {
        Exercise exercise = data.get(position);
        holder.bindTo(exercise);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int id;
        private int cycleId;
        private int routineId;

        private TextView title, repetitions, duration;
        private ImageView repetitionsImage, durationImage;

        public ViewHolder(@NonNull View view) {
            super(view);

            title = itemView.findViewById(R.id.exerciseTitle);
            repetitions = itemView.findViewById(R.id.exerciseRepetitions);
            duration = itemView.findViewById(R.id.exerciseDuration);

            durationImage = itemView.findViewById(R.id.durationImage);
            repetitionsImage = itemView.findViewById(R.id.repetitionsImage);

            int nightModeFlags = durationImage.getContext().getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    durationImage.setImageResource(R.drawable.baseline_timer_white_18dp);
                    repetitionsImage.setImageResource(R.drawable.baseline_replay_white_18dp);
                    break;
            }

            view.setOnClickListener(this);
        }

        public void bindTo(Exercise exercise) {
            id = exercise.getId();
            cycleId=exercise.getCycleId();
            routineId=exercise.getRoutineId();

            title.setText(exercise.getName());
            duration.setText(Integer.toString(exercise.getDuration()));
            repetitions.setText(Integer.toString(exercise.getRepetitions()));
        }

        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(
                AllExercisesFragmentDirections.actionAllExercisesFragmentToExerciseDetailFragment(id,cycleId,routineId)
            );

        }

    }

}
