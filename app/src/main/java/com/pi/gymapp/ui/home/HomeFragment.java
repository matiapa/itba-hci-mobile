package com.pi.gymapp.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.ui.routine_list.RoutineList;
import com.pi.gymapp.ui.routine_list.RoutineListAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // RecyclerViews population

        RecyclerView recyclerView = root.findViewById(R.id.favRoutinesList);
        recyclerView.setAdapter(new RoutineListAdapter(getContext(), homeViewModel.getFavRoutines().getValue(), false));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView = root.findViewById(R.id.allRoutinesList);
        recyclerView.setAdapter(new RoutineListAdapter(getContext(), homeViewModel.getAllRoutines().getValue(), true));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        // Buttons handlers

        root.findViewById(R.id.allFavRoutinesButton).setOnClickListener(c -> {
            Intent intent = new Intent(getActivity(), RoutineList.class)
                    .putExtra("filter", "all");
            startActivity(intent);
        });

        root.findViewById(R.id.allRoutinesButton).setOnClickListener(c -> {
            Intent intent = new Intent(getActivity(), RoutineList.class)
                    .putExtra("filter", "favourites");
            startActivity(intent);
        });

        return root;
    }

    public void seeMoreRoutines(View view){
        startActivity(new Intent(getActivity(), RoutineList.class));
    }

}