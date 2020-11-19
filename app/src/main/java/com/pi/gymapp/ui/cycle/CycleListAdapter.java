package com.pi.gymapp.ui.cycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.ui.routine.RoutineDetailFragmentDirections;
import com.pi.gymapp.utils.StringUtils;

import java.util.List;

public class CycleListAdapter extends RecyclerView.Adapter<CycleListAdapter.ViewHolder> {

    private final List<Cycle> data;

    public CycleListAdapter(List<Cycle> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cycle_list_card, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cycle cycle = data.get(position);
        holder.bindTo(cycle);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int routineId;
        private int cycleId;

        public TextView name, detail, type;

        public ViewHolder(@NonNull View view) {
            super(view);

            name = itemView.findViewById(R.id.cycleName);
            detail = itemView.findViewById(R.id.cycleDetail);
            type = itemView.findViewById(R.id.cycleType);

            view.setOnClickListener(this);
        }

        public void bindTo(Cycle cycle) {
            routineId = cycle.getRoutineId();
            cycleId = cycle.getId();

            name.setText(cycle.getName());
            detail.setText(cycle.getDetail());
            type.setText(StringUtils.capitalize(cycle.getType()));
        }

        @Override
        public void onClick(View v) {
            Navigation.findNavController(v).navigate(
                RoutineDetailFragmentDirections.actionRoutineDetailFragmentToAllExercisesFragment(cycleId, routineId)
            );
        }
    }

}
