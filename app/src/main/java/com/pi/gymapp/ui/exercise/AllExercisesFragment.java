package com.pi.gymapp.ui.exercise;

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

import com.pi.gymapp.databinding.ExerciseListAllBinding;

import com.pi.gymapp.domain.Exercise;

import com.pi.gymapp.repo.ExerciseRepository;

import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.ui.account.SignUpFragment2Args;
import com.pi.gymapp.utils.RepositoryViewModel;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AllExercisesFragment extends Fragment {
    private ExerciseListAllBinding binding;
    private ExerciseViewModel exerciseViewModel;
    private int cycleId;
    private int routineId;
    private boolean observed=false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ExerciseListAllBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        MainActivity activity = (MainActivity) getActivity();


        // --------------------------------- Adapter setup ---------------------------------

        List<Exercise> exercises = new ArrayList<>();
        ExercisesListAdapter adapter = new ExercisesListAdapter(exercises);

        binding.exercisesList.setHasFixedSize(true);
        binding.exercisesList.setLayoutManager(new LinearLayoutManager(activity));


        binding.exercisesList.setAdapter(adapter);


        // --------------------------------- ViewModel setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                ExerciseRepository.class, application.getExerciseRepository()
        );
        exerciseViewModel = new ViewModelProvider(this, viewModelFactory).get(ExerciseViewModel.class);

        cycleId = AllExercisesFragmentArgs.fromBundle(getArguments()).getCycleId();
        routineId = AllExercisesFragmentArgs.fromBundle(getArguments()).getRoutineId();

        exerciseViewModel.setRoutineId(routineId);
        exerciseViewModel.setCycleId(cycleId);

        exerciseViewModel.getExercises().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    exercises.clear();
                    exercises.addAll(resource.data);
                    Collections.sort(exercises);

                    adapter.notifyDataSetChanged();
                    break;
            }
        });

    }

}