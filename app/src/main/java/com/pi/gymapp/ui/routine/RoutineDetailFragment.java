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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutineDetailBinding;
import com.pi.gymapp.databinding.RoutinesListFavsBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;

import java.util.ArrayList;
import java.util.List;


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

        routineViewModel.getRoutine().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();
                    binding.routineDetailcard.routineName.setText(resource.data.getTitle());
                    binding.routineDetailcard.routineRating.setText(
                            String.format(getString(R.string.rateFormat), resource.data.getRate())
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

    }
}
