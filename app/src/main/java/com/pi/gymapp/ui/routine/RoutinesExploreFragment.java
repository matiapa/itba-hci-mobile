package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutinesExploreBinding;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.utils.RepositoryViewModel;


public class RoutinesExploreFragment extends Fragment {

    private RoutinesExploreBinding binding;
    private RoutineViewModel routineViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutinesExploreBinding.inflate(getLayoutInflater());

        MyApplication application = (MyApplication) getActivity().getApplication();


        // --------------------------------- ViewModel setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);


        // --------------------------------- FragmentContainer setup ---------------------------------

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.routineListFragment, new RoutineListFragment(
                routineViewModel.getRoutines()
        )).commit();

        // --------------------------------- Buttons setup ---------------------------------

        binding.allRoutinesChip.setOnClickListener(l -> {
            fragmentManager.beginTransaction().replace(R.id.routineListFragment, new RoutineListFragment(
                    routineViewModel.getRoutines()
            )).commit();
        });

        binding.favRoutinesChip.setOnClickListener(l -> {
            fragmentManager.beginTransaction().replace(R.id.routineListFragment, new RoutineListFragment(
                    routineViewModel.getFavourites()
            )).commit();
        });

        return binding.getRoot();
    }

}
