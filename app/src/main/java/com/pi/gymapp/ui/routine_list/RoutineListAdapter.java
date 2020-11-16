package com.pi.gymapp.ui.routine_list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.data.RoutineEntity;

import java.util.List;

public class RoutineListAdapter extends RecyclerView.Adapter<RoutineListAdapter.ViewHolder> {

    private Context context;
    private List<RoutineEntity> routines;
    private boolean showFavButton;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoutineEntity entity;
        public TextView title, rate, duration;

        public ViewHolder(View view) {
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
        }
    }

    public RoutineListAdapter(Context context, List<RoutineEntity> routines, boolean showFavButton) {
        this.context = context;
        this.routines = routines;
        this.showFavButton = showFavButton;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.routine_card, parent, false);

        view.findViewById(R.id.favButton).setVisibility(showFavButton ? View.VISIBLE : View.INVISIBLE);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.entity = routines.get(i);

        holder.title.setText(routines.get(i).title);
        holder.duration.setText(String.format(context.getString(R.string.secondsFormat), routines.get(i).duration));
        holder.rate.setText(String.format(context.getString(R.string.rateFormat), routines.get(i).rate));
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }
}