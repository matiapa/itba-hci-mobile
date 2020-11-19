package com.pi.gymapp.ui.account;

import android.content.Intent;
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
//    static boolean isAgeValid(String age) {
//        String regex = "^[0-9]+$";
//        return age.matches(regex);
//    }

    
    public void finishSignUp(View view){

//        Bundle bundle = SignUpFragment2Args.fromBundle(getArguments());

        assert getArguments() != null;
        if (SignUpFragment2Args.fromBundle(getArguments()).getEmail()==null || SignUpFragment2Args.fromBundle(getArguments()).getPassword()==null || SignUpFragment2Args.fromBundle(getArguments()).getUsername()==null)
            throw new IllegalArgumentException("no estoy recibiendo mis argumentos del otro lado");
        ApiUserService userService= ApiClient.create(getContext(),ApiUserService.class);
        EditText name =binding.signUpName;
        EditText phone = binding.signUpPhone;
        DatePicker age =binding.datePicker1;
        String gender =(binding.sexSpinner).getSelectedItem().toString();


        if (!(isNameValid(name.getText().toString()) && isPhoneValid(phone.getText().toString()) ) )
        {
            Snackbar.make(view, "Invalid Input", Snackbar.LENGTH_LONG).show();
            return;
        }

        Calendar cal=Calendar.getInstance();
        cal.clear();
        cal.set(age.getYear(),age.getMonth(),age.getDayOfMonth());
        userService.createUser(new UserData(
                SignUpFragment2Args.fromBundle(getArguments()).getUsername(),
                name.getText().toString(),
                gender.toLowerCase(),
                cal.getTimeInMillis(),
                SignUpFragment2Args.fromBundle(getArguments()).getEmail(),
                phone.getText().toString(),
                null,
                SignUpFragment2Args.fromBundle(getArguments()).getPassword()
                )).observe(this, r->{
            if (r.getError()!= null){
                Snackbar.make(view, "Ups! Something went wrong", Snackbar.LENGTH_LONG).show();
            }else {

                SignUpFragment2Directions.ActionSignUpFragment2ToVerifyEmail action = SignUpFragment2Directions.actionSignUpFragment2ToVerifyEmail();
                action.setEmail(SignUpFragment2Args.fromBundle(getArguments()).getEmail());
                NavHostFragment.findNavController(this).navigate(action);
//        startActivity(new Intent(this, VerifyEmail.class).putExtra("email","test@tes.com"));
                //startActivity(new Intent(this, VerifyEmail.class).putExtra("email",bundle.getString("email")));
            }
        });


    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = SignUpS2Binding.inflate(getLayoutInflater());
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSignUp(v);
            }
        });
        return binding.getRoot();
    }


}