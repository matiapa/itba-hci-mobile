package com.pi.gymapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.pi.gymapp.databinding.FragmentSettingsBinding;
import com.pi.gymapp.ui.MainActivity;


public class BlankFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private MainActivity activity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());


        activity = (MainActivity) getActivity();
//
        activity.getSupportActionBar().setTitle(R.string.settings);
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
            binding.switch1.setChecked(true);


        binding.switch1.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   if (AppCompatDelegate.getDefaultNightMode()!=AppCompatDelegate.MODE_NIGHT_YES) {
                       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                       binding.switch1.setChecked(true);
                   } else{
                       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                       binding.switch1.setChecked(false);
                   }
               }
           }

        );
        return binding.getRoot();
    }
}