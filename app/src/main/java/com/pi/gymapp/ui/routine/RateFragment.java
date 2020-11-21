package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.api.models.ReviewModel;
import com.pi.gymapp.databinding.RateBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;

public class RateFragment extends DialogFragment {

    RateBinding binding;
    private RoutineViewModel routineViewModel;
    private int routineId;

    MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RateBinding.inflate(getLayoutInflater());
//        routineId = RoutineDetailFragmentArgs.fromBundle(getArguments()).getRoutineId();

        routineId = getArguments().getInt("routineId");

        return binding.getRoot();
    };

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


        // --------------------------------- Data load ---------------------------------

        routineViewModel.getRoutine(routineId).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();
                    Routine r = resource.data;

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });


        // --------------------------------- Buttons setup ---------------------------------

        binding.sumbitButton.setOnClickListener(v -> {
            ReviewModel review = new ReviewModel(
                    binding.review.getText().toString(), binding.ratingBar.getRating()
            );

            routineViewModel.postReview(routineId, review).observe(getViewLifecycleOwner(), resource -> {
                switch (resource.status) {
                    case LOADING:
                        activity.showProgressBar();
                        break;

                    case SUCCESS:
                        activity.hideProgressBar();

                        Toast.makeText(activity, getString(R.string.success_message), Toast.LENGTH_SHORT).show();

                        dismiss();

                        break;

                    case ERROR:
                        activity.hideProgressBar();
                        Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            });
        });


        binding.cancelButton.setOnClickListener(v -> {
            dismiss();
        });
    }

}
