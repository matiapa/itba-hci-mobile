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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutinesListAllBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;

import java.util.ArrayList;
import java.util.List;


public class AllRoutinesFragment extends Fragment {

    private RoutinesListAllBinding binding;
    private RoutineViewModel routineViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutinesListAllBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        MainActivity activity = (MainActivity) getActivity();


        // --------------------------------- Adapter setup ---------------------------------

        List<Routine> routines = new ArrayList<>();
        RoutinesListAdapter adapter = new RoutinesListAdapter(routines);

        binding.routinesList.setHasFixedSize(true);
        binding.routinesList.setLayoutManager(new LinearLayoutManager(activity));
        /*binding.routinesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!binding.routinesList.canScrollVertically(1)) {
                    routineViewModel.getMoreRoutines();
                }
            }
        });*/

        binding.routinesList.setAdapter(adapter);


        // --------------------------------- ViewModel setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        //routineViewModel.resetRoutinesList();

        routineViewModel.getRoutines().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    routines.clear();
                    routines.addAll(resource.data);

                    adapter.notifyDataSetChanged();
                    //binding.routinesList.scrollToPosition(routines.size() - 1);
                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });


        // --------------------------------- Buttons setup ---------------------------------

        binding.favRoutinesChip.setOnClickListener(l ->
                Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_favRoutinesFragment)
        );
    }
}
