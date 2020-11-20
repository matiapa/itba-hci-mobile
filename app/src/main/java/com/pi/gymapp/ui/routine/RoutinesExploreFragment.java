package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.util.Log;
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

import java.util.Collections;
import java.util.Map;


public class RoutinesExploreFragment extends Fragment {

    private RoutinesExploreBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutinesExploreBinding.inflate(getLayoutInflater());

        MyApplication application = (MyApplication) getActivity().getApplication();


        // --------------------------------- FragmentContainer setup ---------------------------------

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


        RoutineListFragment routineListFragment = newFragment(RoutineListFragment.class,
                new String[]{"filter"}, new String[]{"all"});
        fragmentManager.beginTransaction().add(R.id.routineListFragment, routineListFragment).commit();


        // --------------------------------- Buttons setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                RoutineRepository.class, application.getRoutineRepository()
        );
        RoutineViewModel routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        binding.allRoutinesChip.setOnClickListener(l -> {
            RoutineListFragment fragment = newFragment(RoutineListFragment.class,
                    new String[]{"filter"}, new String[]{"all"});
            fragmentManager.beginTransaction().add(R.id.routineListFragment, fragment).commit();
        });

        binding.favRoutinesChip.setOnClickListener(l -> {
            RoutineListFragment fragment = newFragment(RoutineListFragment.class,
                    new String[]{"filter"}, new String[]{"favourites"});
            fragmentManager.beginTransaction().add(R.id.routineListFragment, fragment).commit();
        });

        return binding.getRoot();
    }


    private <T extends Fragment> T newFragment(Class<T> fragmentClass, String[] keys, String[] args){
        Bundle bundle = new Bundle();
        for(int i=0; i<keys.length; i++)
            bundle.putString(keys[i], args[i]);

        try{
            T fragment = fragmentClass.newInstance();
            fragment.setArguments(bundle);
            return fragment;
        }catch(Exception e){
            Log.e("Fragment", e.getMessage());
            return null;
        }
    }

}
