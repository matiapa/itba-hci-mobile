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
import com.pi.gymapp.db.entity.RoutineEntity;
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

//        view.findViewById(R.id.favButton).setVisibility(showFavButton ? View.VISIBLE : View.INVISIBLE);

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
        public RoutineEntity entity;
        public TextView title, rate, duration;

        public ViewHolder(@NonNull View view) {
            super(view);

            title = itemView.findViewById(R.id.routineName);
            rate = itemView.findViewById(R.id.routineRating);
            duration = itemView.findViewById(R.id.routineDuration);

            ImageButton favButton = itemView.findViewById(R.id.favButton);
            favButton.setOnClickListener(l -> {
                entity.isFav = !entity.isFav;

                favButton.setImageResource(entity.isFav
                        ? R.drawable.activity_fav_button_active : R.drawable.activity_fav_button);
            });

//            view.setOnClickListener(this);
        }

        public void bindTo(Routine routine) {
            id = routine.getId();
            title.setText(routine.getTitle());

            Context context = title.getContext();
            rate.setText(String.format(context.getString(R.string.rateFormat), routine.getRate()));
        }

//        @Override
//        public void onClick(View v) {
//            Context context = textView.getContext();
//            if (context instanceof MainActivity) {
//                MainActivity activity = (MainActivity)context;
//                activity.replaceFragment(RoutineFragment.create(id, false), true);
//            }
//        }
    }

}
