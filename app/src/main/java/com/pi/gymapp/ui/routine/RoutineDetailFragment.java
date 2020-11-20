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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.databinding.RoutineDetailBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.ui.cycle.CycleListFragment;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.StringUtils;


public class RoutineDetailFragment extends Fragment {

    RoutineDetailBinding binding;
    private RoutineViewModel routineViewModel;
    private int routineId;
    private Boolean isFav;

    MainActivity activity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RoutineDetailBinding.inflate(getLayoutInflater());

        binding.buttonFav.setEnabled(false);
        binding.buttonFav.setClickable(false);

        binding.buttonShare.setOnClickListener(view -> {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.gymapp.com/id/"+routineId);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

        });

        routineId = RoutineDetailFragmentArgs.fromBundle(getArguments()).getRoutineId();

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
        activity = (MainActivity) getActivity();

        // --------------------------------- ViewModel setup ---------------------------------

        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
            RoutineRepository.class, application.getRoutineRepository()
        );
        routineViewModel = new ViewModelProvider(this, viewModelFactory).get(RoutineViewModel.class);


        // --------------------------------- Fetch if is fav ---------------------------------

        routineViewModel.fetchIsFav(routineId).observe(getViewLifecycleOwner(), isFav -> {
            this.isFav = isFav;

            binding.buttonFav.setClickable(true);
            binding.buttonFav.setEnabled(true);

            if(isFav)
                binding.buttonFav.setImageResource(R.drawable.fav_button_active);
            else
                binding.buttonFav.setImageResource(R.drawable.fav_button);
        });


        routineViewModel.getRoutine(routineId).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
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
            Navigation.findNavController(view).navigate(
                    RoutineDetailFragmentDirections.actionRoutineDetailFragmentToPlayRoutineFragment(routineId)
            );
        });

        binding.buttonFav.setOnClickListener(v -> {
            isFav = !isFav;

            routineViewModel.setFav(routineId, isFav).observe(getViewLifecycleOwner(), res -> {
                if(res.getError() != null)
                    Toast.makeText(activity, getResources().getString(R.string.unexpected_error),
                            Toast.LENGTH_SHORT).show();
            });

            if(isFav)
                binding.buttonFav.setImageResource(R.drawable.fav_button_active);
            else
                binding.buttonFav.setImageResource(R.drawable.fav_button);
        });

    }

}