package com.pi.gymapp.ui.routine;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.databinding.RecyclerViewBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;


public class RoutineListFragment extends Fragment {

    private RecyclerViewBinding binding;
    private RoutineViewModel routineViewModel;

    MyApplication application;
    MainActivity activity;

    List<Routine> routines;
    RoutinesListAdapter adapter;

    LiveData<Resource<List<Routine>>> routinesLiveData;

    RoutineOrder routineOrder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (MyApplication) getActivity().getApplication();
        activity = (MainActivity) getActivity();

        routines = new ArrayList<>();
        adapter = new RoutinesListAdapter(routines);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RecyclerViewBinding.inflate(getLayoutInflater());


        // --------------------------------- ViewModel setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);


        String filter = getArguments().getString("filter");

        Integer orderIndex = Integer.valueOf(getArguments().getString("orderBy"));
        routineOrder = RoutineOrder.values()[orderIndex];

        if(filter.equals("all")){
            routinesLiveData = routineViewModel.getRoutines(routineOrder.getFieldName(), "desc");
        }else{
            routinesLiveData = routineViewModel.getFavourites();
        }


        // --------------------------------- RecyclerView setup ---------------------------------

        int orientation = getActivity().getResources().getConfiguration().orientation == 1
                ? RecyclerView.VERTICAL
                : RecyclerView.HORIZONTAL;

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(activity, orientation, false));
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        routinesLiveData.observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    routines.clear();
                    routines.addAll(resource.data);

                    routines.sort(routineOrder.getComparator());

                    adapter.notifyDataSetChanged();
                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

}
