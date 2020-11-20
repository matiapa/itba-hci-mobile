package com.pi.gymapp.ui.profile;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.util.StringUtil;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.MyApplication;
import com.pi.gymapp.R;
import com.pi.gymapp.api.models.UserData;
import com.pi.gymapp.databinding.FragmentProfileBinding;
import com.pi.gymapp.domain.Routine;
import com.pi.gymapp.domain.User;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.repo.UserRepository;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.RepositoryViewModel;
import com.pi.gymapp.utils.StringUtils;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    private MainActivity activity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());


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

                    binding.userUsername.setText(StringUtils.capitalize(r.getUsername()));
                    binding.userEmail.setText(StringUtils.capitalize(r.getEmail()));

                    binding.userPhone.setText(StringUtils.capitalize(Long.toString(r.getPhone())));
                    binding.userBirthday.setText(String.format(getString(R.string.dateFormat), r.getBirthdate()));

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

                    binding.radioButtonOther.setEnabled(false);
                    binding.radioButtonMale.setEnabled(false);
                    binding.radioButtonFemale.setEnabled(false);

                    break;

                case ERROR:
                    activity.hideProgressBar();
                    Toast.makeText(activity, resource.message, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        });

        binding.profileButtonEdit.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_profileFragment2)
        );
    }
}