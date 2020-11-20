package com.pi.gymapp.ui.routine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutineDetailBinding;
import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.ui.account.SignUpFragment1Directions;
import com.pi.gymapp.ui.cycle.CycleListAdapter;
import com.pi.gymapp.ui.cycle.CycleListFragment;
import com.pi.gymapp.ui.exercise.AllExercisesFragment;
import com.pi.gymapp.ui.exercise.AllExercisesFragmentArgs;
import com.pi.gymapp.ui.exercise.AllExercisesFragmentDirections;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class RoutineDetailFragment extends Fragment {

    RoutineDetailBinding binding;
    private RoutineViewModel routineViewModel;
    private int routineId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutineDetailBinding.inflate(getLayoutInflater());
        routineId = RoutineDetailFragmentArgs.fromBundle(getArguments()).getRoutineId();

        binding.getRoot().findViewById(R.id.button_share).setOnClickListener(view -> {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.gymapp.com/id/"+routineId);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        });

        // --------------------------------- Cycle list fragment setup ---------------------------------

        Bundle args = new Bundle();
        args.putInt("routineId", routineId);

        CycleListFragment cycleListFragment = new CycleListFragment();
        cycleListFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.cycleListFragment, cycleListFragment).commit();

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        MainActivity activity = (MainActivity) getActivity();

        // --------------------------------- ViewModel setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
            RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);

        routineViewModel.setRoutineId(routineId);

        routineViewModel.getRoutine().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    Routine r = resource.data;

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());

                    binding.routineCategoryChip.setText(StringUtils.capitalize(r.getCategoryName()));
                    binding.routineDifficultyChip.setText(StringUtils.capitalize(r.getDifficulty()));
                    binding.routineRateChip.setText(String.format(getString(R.string.rateFormat), r.getRate()));

                    binding.routineDescription.setText(r.getDetail());

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        });


        // --------------------------------- Button handlers setup ---------------------------------

        binding.playButton.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(RoutineDetailFragmentDirections.actionRoutineDetailFragmentToPlayRoutineFragment(routineId));
        });


    }
}
