package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.databinding.FragmentFavRoutinesBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavRoutinesFragment extends Fragment {

    FragmentFavRoutinesBinding binding;
    private RoutineViewModel routineViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavRoutinesBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        MainActivity activity = (MainActivity) getActivity();

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
            RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);


        List<Routine> routines = new ArrayList<>();
        RoutineAdapter adapter = new RoutineAdapter(routines);

        routineViewModel.getFavourites().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    routines.clear();
                    routines.addAll(resource.data);

                    adapter.notifyDataSetChanged();
                    binding.routinesList.scrollToPosition(routines.size() - 1);
                    break;
            }
        });

        binding.routinesList.setHasFixedSize(true);
        binding.routinesList.setLayoutManager(new LinearLayoutManager(activity));
        binding.routinesList.setAdapter(adapter);
    }
}
