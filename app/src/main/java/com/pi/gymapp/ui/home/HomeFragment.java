package com.pi.gymapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pi.gymapp.R;
import com.pi.gymapp.ui.RoutineList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        root.findViewById(R.id.allFavRoutinesButton).setOnClickListener(c -> {
            startActivity(new Intent(getActivity(), RoutineList.class));
        });

        root.findViewById(R.id.allRoutinesButton).setOnClickListener(c -> {
            startActivity(new Intent(getActivity(), RoutineList.class));
        });

        return root;
    }

    public void seeMoreRoutines(View view){
        startActivity(new Intent(getActivity(), RoutineList.class));
    }

}