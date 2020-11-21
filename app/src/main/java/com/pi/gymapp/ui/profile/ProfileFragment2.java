package com.pi.gymapp.ui.profile;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.api.models.UserChangeData;
import com.pi.gymapp.api.models.UserData;

import com.pi.gymapp.databinding.FragmentProfileEditBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.domain.User;
import com.pi.gymapp.repo.UserRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.StringUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileFragment2 extends Fragment{

    FragmentProfileEditBinding binding;
    private ProfileViewModel profileViewModel;

    private MainActivity activity;

    private long birthday;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(getLayoutInflater());

        activity = (MainActivity) getActivity();

        MyApplication application = (MyApplication) getActivity().getApplication();
        ViewModelProvider.Factory viewModelFactory = new RepositoryViewModel.Factory<>(
                UserRepository.class, application.getUserRepository()
        );

        profileViewModel = new ViewModelProvider(this, viewModelFactory).get(ProfileViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        profileViewModel.getUser().observe(getViewLifecycleOwner(), resource -> {
            switch (resource.status) {
                case LOADING:
                    activity.showProgressBar();
                    break;

                case SUCCESS:
                    activity.hideProgressBar();

                    User r = resource.data;
                    birthday = r.getBirthdate();

                    binding.userUsername.setText(StringUtils.capitalize(r.getUsername()));
                    binding.userEmail.setText(StringUtils.capitalize(r.getEmail()));

                    binding.userPhone.setText(StringUtils.capitalize(Long.toString(r.getPhone())));

                    DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(binding.userBirthday.getContext());
                    binding.userBirthday.setText(dateFormat.format(new Date( r.getBirthdate())));

                    binding.userFullnameAgain.setText(StringUtils.capitalize(r.getFullName()));
                    binding.userFullname.setText(StringUtils.capitalize(r.getFullName()));

                    if(r.getGender().equals("male")) {

                        binding.radioButtonMale.setChecked(true);
                        binding.radioButtonFemale.setChecked(false);
                        binding.radioButtonOther.setChecked(false);

                    } else if (r.getGender().equals("female")) {

                        binding.radioButtonMale.setChecked(false);
                        binding.radioButtonFemale.setChecked(true);
                        binding.radioButtonOther.setChecked(false);

                    } else {

                        binding.radioButtonMale.setChecked(false);
                        binding.radioButtonFemale.setChecked(false);
                        binding.radioButtonOther.setChecked(true);

                    }

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        });

        binding.profileButtonSave.setOnClickListener(v -> {
            String gender;

            if(binding.radioButtonMale.isChecked()) {
                gender = "male";
            } else if (binding.radioButtonFemale.isChecked()) {
                gender = "female";
            } else {
                gender = "other";
            }

            UserChangeData data = new UserChangeData(
                binding.userUsername.getText().toString(),
                binding.userFullnameAgain.getText().toString(),
                gender, birthday,
                binding.userEmail.getText().toString(),
                binding.userPhone.getText().toString(),
                        null
            );

            profileViewModel.sendUserChange(data).observe(getViewLifecycleOwner(), resource->{
                switch (resource.status) {
                    case LOADING:
                        activity.showProgressBar();
                        break;

                    case SUCCESS:
                        activity.hideProgressBar();
                        Navigation.findNavController(v).navigate(
                                ProfileFragment2Directions.actionProfileFragment2ToNavProfile()
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
        });

        binding.profileButtonCancel.setOnClickListener(v ->
            Navigation.findNavController(v).navigate(
                    ProfileFragment2Directions.actionProfileFragment2ToNavProfile()
            )
        );

    }
}
