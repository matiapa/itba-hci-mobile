package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.models.UserData;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.databinding.SignInBinding;
import com.pi.gymapp.databinding.SignUpS1Binding;
import com.pi.gymapp.databinding.SignUpS2Binding;
import com.pi.gymapp.domain.User;

import java.util.Calendar;
import java.util.Date;

public class SignUpFragment2 extends Fragment {

    SignUpS2Binding binding;

    static boolean isNameValid(String name) {
        String regex = "^[a-zA-Z ]+$";
        return name.matches(regex);
    }

    static boolean isPhoneValid(String phone) {
        String regex = "^([+])?[0-9]{8,15}$";
        return phone.matches(regex);
    }

    
    public void finishSignUp(View view){

        binding.loading.setVisibility(View.VISIBLE);
        binding.signInButton.setEnabled(false);

        assert getArguments() != null;
        if (SignUpFragment2Args.fromBundle(getArguments()).getEmail()==null
                || SignUpFragment2Args.fromBundle(getArguments()).getPassword()==null
                || SignUpFragment2Args.fromBundle(getArguments()).getUsername()==null)
            throw new IllegalArgumentException("no estoy recibiendo mis argumentos del otro lado");

        ApiUserService userService = ApiClient.create(getContext(),ApiUserService.class);

        EditText name = binding.signUpName;
        EditText phone = binding.signUpPhone;
        DatePicker age = binding.datePicker1;
        String gender = (binding.sexSpinner).getSelectedItem().toString();

        if (!isNameValid(name.getText().toString())){
            Snackbar.make(view, R.string.name_fail, Snackbar.LENGTH_LONG).show();
            return;
        }

        if (!isNameValid(name.getText().toString()) || !isPhoneValid(phone.getText().toString())){
            Snackbar.make(view, R.string.phone_fail, Snackbar.LENGTH_LONG).show();
            return;
        }

        Calendar cal=Calendar.getInstance();
        cal.clear();
        cal.set(age.getYear(),age.getMonth(),age.getDayOfMonth());

        UserData newUser = new UserData(
            SignUpFragment2Args.fromBundle(getArguments()).getUsername(),
            name.getText().toString(),
            gender.toLowerCase(),
            cal.getTimeInMillis(),
            SignUpFragment2Args.fromBundle(getArguments()).getEmail(),
            phone.getText().toString(),
            null,
            SignUpFragment2Args.fromBundle(getArguments()).getPassword()
        );

        userService.createUser(newUser).observe(getViewLifecycleOwner(), r->{
            binding.loading.setVisibility(View.GONE);
            binding.signInButton.setEnabled(true);

            if (r.getError()!= null){

                Snackbar.make(view, R.string.general_error, Snackbar.LENGTH_LONG).show();

            }else {

                SignUpFragment2Directions.ActionSignUpFragment2ToVerifyEmail action =
                        SignUpFragment2Directions.actionSignUpFragment2ToVerifyEmail();
                action.setEmail(SignUpFragment2Args.fromBundle(getArguments()).getEmail());

                NavHostFragment.findNavController(this).navigate(action);

            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = SignUpS2Binding.inflate(getLayoutInflater());
        binding.signInButton.setOnClickListener(v -> finishSignUp(v));

        int nightModeFlags = getContext().getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                binding.datePicker1.setBackgroundColor(getResources().getColor(R.color.colorLight));
                break;
        }

        return binding.getRoot();
    }


}