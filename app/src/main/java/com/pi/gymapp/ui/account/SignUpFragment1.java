package com.pi.gymapp.ui.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.databinding.SignInBinding;
import com.pi.gymapp.databinding.SignUpS1Binding;

public class SignUpFragment1 extends Fragment {

    SignUpS1Binding binding;

    static boolean isEmailValid(String email) {
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    static boolean isUsernameValid(String username) {
        return username != null && !username.equals("");
    }

    static boolean isPasswordValid(String pass) {
        return pass.length() >= 8;
    }

    public void nextStep(View view){

        EditText user_input = binding.UsernameSignup;
        EditText email_input = binding.EmailSignup;
        EditText password_input = binding.passwordSignup;

        if (!isEmailValid(email_input.getText().toString())){
            Snackbar.make(view, R.string.email_fail, Snackbar.LENGTH_LONG).show();
            return;
        }else if (!isUsernameValid(user_input.getText().toString())){
            Snackbar.make(view, R.string.username_fail, Snackbar.LENGTH_LONG).show();
            return;
        }else if (!isPasswordValid(password_input.getText().toString())){
            Snackbar.make(view, R.string.Password_fail,
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        SignUpFragment1Directions.ActionSignUpFragment1ToSignUpFragment2 action =
                SignUpFragment1Directions.actionSignUpFragment1ToSignUpFragment2();

        action.setEmail(email_input.getText().toString());
        action.setPassword(password_input.getText().toString());
        action.setUsername(user_input.getText().toString());

        NavHostFragment.findNavController(this).navigate(action);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        binding = SignUpS1Binding.inflate(getLayoutInflater());

        binding.signInButton.setOnClickListener(this::nextStep);

        return binding.getRoot();
    }


}