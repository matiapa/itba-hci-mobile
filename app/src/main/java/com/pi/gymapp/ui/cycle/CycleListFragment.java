package com.pi.gymapp.ui.cycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.databinding.CycleListRoutineBinding;
import com.pi.gymapp.databinding.RoutineDetailBinding;
import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.repo.CycleRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;

import java.util.ArrayList;
import java.util.List;


public class CycleListFragment extends Fragment {

    CycleListRoutineBinding binding;
    private CycleViewModel cycleViewModel;

    private int routineId;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = CycleListRoutineBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        MainActivity activity = (MainActivity) getActivity();

        // --------------------------------- Adapter setup ---------------------------------

        List<Cycle> cycles = new ArrayList<>();
        CycleListAdapter adapter = new CycleListAdapter(cycles);

        binding.cyclesList.setHasFixedSize(true);
        binding.cyclesList.setLayoutManager(new LinearLayoutManager(activity));
        binding.cyclesList.setAdapter(adapter);


        // --------------------------------- ViewModel setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
            CycleRepository.class, application.getCycleRepository()
        );
        cycleViewModel = new ViewModelProvider(this, viewModelFactory).get(CycleViewModel.class);

        routineId = getArguments().getInt("routineId");

        cycleViewModel.setRoutineId(routineId);

        cycleViewModel.getCycles().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    cycles.clear();
                    cycles.addAll(resource.data);
                    cycles.sort(Cycle::compareTo);

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
