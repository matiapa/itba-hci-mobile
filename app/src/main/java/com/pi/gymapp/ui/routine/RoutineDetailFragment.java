package com.pi.gymapp.ui.routine;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutineDetailBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.ui.account.SignUpFragment1Directions;
import com.pi.gymapp.ui.exercise.AllExercisesFragment;
import com.pi.gymapp.ui.exercise.AllExercisesFragmentArgs;
import com.pi.gymapp.ui.exercise.AllExercisesFragmentDirections;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.StringUtils;


public class RoutineDetailFragment extends Fragment {

    RoutineDetailBinding binding;
    private RoutineViewModel routineViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutineDetailBinding.inflate(getLayoutInflater());

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

        routineViewModel.setRoutineId(RoutineDetailFragmentArgs.fromBundle(getArguments()).getRoutineId());

        //TODO conectar esta variable
        int cyleId =1;

        routineViewModel.getRoutine().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(
                            resource.data.getName()
                    );

                    Routine r = resource.data;

                    binding.routineCategoryChip.setText(
                            StringUtils.capitalize(r.getCategoryName())
                    );
                    binding.routineDifficultyChip.setText(
                            StringUtils.capitalize(r.getDifficulty())
                    );
                    binding.routineRateChip.setText(
                            String.format(getString(R.string.rateFormat), r.getRate())
                    );

                    binding.routineDescription.setText(
                            r.getDetail()
                    );

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        });

        binding.playButton.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        );

        binding.warmUpButton.setOnClickListener(view ->{

            RoutineDetailFragmentDirections.ActionRoutineDetailFragmentToAllExercisesFragment action = RoutineDetailFragmentDirections.actionRoutineDetailFragmentToAllExercisesFragment(routineViewModel.getRoutine().getValue().data.getId(),cyleId);

            NavHostFragment.findNavController(this).navigate(action);
        });

    }
}
