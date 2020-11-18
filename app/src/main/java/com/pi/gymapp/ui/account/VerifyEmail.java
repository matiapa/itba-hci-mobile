package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.api.ApiUserService;

import com.pi.gymapp.api.models.Email;
import com.pi.gymapp.api.models.VerifyEmailData;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.databinding.SignUpS1Binding;
import com.pi.gymapp.databinding.SignUpVerifyEmailBinding;
import com.pi.gymapp.ui.MainActivity;

public class VerifyEmail extends Fragment {
    SignUpVerifyEmailBinding binding;

    public void verify(View view){


        if (VerifyEmailArgs.fromBundle(getArguments()).getEmail()==null)
            throw new IllegalStateException("no estoy recibiendo mis argumentos del otro lado");

        ApiUserService userService = ApiClient.create(getContext(),ApiUserService.class);
        EditText verification_code = binding.VerificationCodeEdit;

        if (verification_code.getText().toString().equals("") )
        {
            Snackbar.make(view, "Invalid Input", Snackbar.LENGTH_LONG).show();
            return;
        }
        userService.verifyEmail(new VerifyEmailData(VerifyEmailArgs.fromBundle(getArguments()).getEmail(),verification_code.getText().toString())).observe(this, r-> {
            if (r.getError() != null) {
                Snackbar.make(view, "Invalid Code", Snackbar.LENGTH_LONG).show();
            } else {
                Intent intent=new Intent(getContext(), MainActivity.class);
                intent.putExtra("login",true);
                startActivity(intent);
                getActivity().finish();

            }

        });
    }

    public void resendVerification(View view){


        if (VerifyEmailArgs.fromBundle(getArguments()).getEmail()==null)
            throw new IllegalStateException("no estoy recibiendo mis argumentos del otro lado");

        ApiUserService userService= ApiClient.create(getContext(),ApiUserService.class);

        userService.resendVerificationEmail(new Email(VerifyEmailArgs.fromBundle(getArguments()).getEmail())).observe(this, r-> {
            if (r.getError() != null) {
                Snackbar.make(view, "Ups! Something went wrong", Snackbar.LENGTH_LONG).show();
            } else {

                Snackbar.make(view, "Email has been sent!", Snackbar.LENGTH_LONG).show();

            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = SignUpVerifyEmailBinding.inflate(getLayoutInflater());
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify(v);
            }
        });
        binding.resendVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerification(v);
            }
        });
        return binding.getRoot();
    }
}
