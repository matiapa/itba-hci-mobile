package com.pi.gymapp.ui.routine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.api.models.ReviewModel;
import com.pi.gymapp.databinding.RateBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.ui.MainActivity;

public class RateFragment extends Fragment {

    RateBinding binding;
    private RoutineViewModel routineViewModel;
    private int routineId;

    MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RateBinding.inflate(getLayoutInflater());
        routineId = RoutineDetailFragmentArgs.fromBundle(getArguments()).getRoutineId();

        return binding.getRoot();
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyApplication application = (MyApplication) getActivity().getApplication();
        activity = (MainActivity) getActivity();

        routineViewModel.getRoutine(routineId).observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    Routine r = resource.data;

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(r.getName());

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        });

        binding.sumbitButton.setOnClickListener(v -> {
            ReviewModel review = new ReviewModel(
                    binding.review.getText().toString(), binding.ratingBar.getNumStars()
            );

            routineViewModel.sendReview(review).observe(getViewLifecycleOwner(), resource->{
                switch (resource.status) {
                    case LOADING:
                        activity.showProgressBar();
                        break;

                    case SUCCESS:
                        activity.hideProgressBar();
                        Navigation.findNavController(v).navigate(
                                RateFragmentDirections
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

            );
                }
                );

        binding.cancelButton.setOnClickListener(v ->
//                Navigation.findNavController(v).navigate(
//                        TODO me falta poner como volver para atras
//                )
        );
    }

}
