package com.pi.gymapp.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.pi.gymapp.R;

public class ProfileFragment2 extends Fragment{


    private ProfileViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel =new ViewModelProvider(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile_edit, container, false);

//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        root.findViewById(R.id.profileButtonSave).setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_profileFragment2_to_nav_profile));

       // getActivity().getSupportFragmentManager().popBackStack();

        return root;
    }

}
