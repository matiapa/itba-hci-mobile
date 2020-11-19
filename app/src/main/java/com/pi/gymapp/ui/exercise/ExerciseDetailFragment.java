package com.pi.gymapp.ui.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.ExerciseDetailBinding;
import com.pi.gymapp.domain.Exercise;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.ExerciseRepository;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.ui.routine.RoutineDetailFragmentArgs;
import com.pi.gymapp.ui.routine.RoutineDetailFragmentDirections;
import com.pi.gymapp.ui.routine.RoutineViewModel;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.StringUtils;


public class ExerciseDetailFragment extends Fragment {

    ExerciseDetailBinding binding;
    private ExerciseViewModel exerciseViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ExerciseDetailBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        MainActivity activity = (MainActivity) getActivity();

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                ExerciseRepository.class, application.getExerciseRepository()
        );
        exerciseViewModel = new ViewModelProvider(this, viewModelFactory).get(ExerciseViewModel.class);

        exerciseViewModel.setRoutineId(ExerciseDetailFragmentArgs.fromBundle(getArguments()).getRoutineId());
        exerciseViewModel.setCycleId(ExerciseDetailFragmentArgs.fromBundle(getArguments()).getCycleId());
        exerciseViewModel.setExerciseId(ExerciseDetailFragmentArgs.fromBundle(getArguments()).getExerciseId());


        exerciseViewModel.getExercise().observe(getViewLifecycleOwner(),resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(
                            resource.data.getName()
                    );

                    Exercise Exe = resource.data;

                    binding.exerciseTitle.setText(resource.data.getName());
                    binding.exerciseDetail.setText(resource.data.getDetail());

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        });


    }

}
