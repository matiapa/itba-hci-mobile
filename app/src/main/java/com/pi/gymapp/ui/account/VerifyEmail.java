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

        binding.loading.setVisibility(View.VISIBLE);
        binding.signInButton.setEnabled(false);
        binding.resendVerificationButton.setEnabled(false);

        ApiUserService userService = ApiClient.create(getContext(),ApiUserService.class);
        EditText verification_code = binding.VerificationCodeEdit;
        EditText verification_email = binding.VerificationEmailEdit;

        if (verification_code.getText().toString().equals("") ) {
            Snackbar.make(view, R.string.email_fail, Snackbar.LENGTH_LONG).show();
            return;
        } else if (verification_code.getText().toString().equals("") ) {
            Snackbar.make(view, R.string.Code_fail, Snackbar.LENGTH_LONG).show();
            return;
        }

        VerifyEmailData verifyEmailData = new VerifyEmailData(
            verification_email.getText().toString(),
            verification_code.getText().toString()
        );

        userService.verifyEmail(verifyEmailData).observe(getViewLifecycleOwner(), r-> {
            binding.loading.setVisibility(View.VISIBLE);
            binding.signInButton.setEnabled(true);
            binding.resendVerificationButton.setEnabled(true);

            if (r.getError() != null) {

                Snackbar.make(view, R.string.Code_fail, Snackbar.LENGTH_LONG).show();

            } else {

                Intent intent=new Intent(getContext(), MainActivity.class);
                intent.putExtra("login",true);

                startActivity(intent);

                getActivity().finish();

            }

        });

    }

    public void resendVerification(View view){

        binding.loading.setVisibility(View.VISIBLE);
        binding.signInButton.setEnabled(false);
        binding.resendVerificationButton.setEnabled(false);

        if (VerifyEmailArgs.fromBundle(getArguments()).getEmail()==null)
            throw new IllegalStateException("no estoy recibiendo mis argumentos del otro lado");

        ApiUserService userService= ApiClient.create(getContext(),ApiUserService.class);

        Email email = new Email(VerifyEmailArgs.fromBundle(getArguments()).getEmail());

        userService.resendVerificationEmail(email).observe(getViewLifecycleOwner(), r-> {
            binding.loading.setVisibility(View.GONE);
            binding.signInButton.setEnabled(true);
            binding.resendVerificationButton.setEnabled(true);

            if (r.getError() != null) {
                Snackbar.make(view, R.string.general_error, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, R.string.email_fail, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = SignUpVerifyEmailBinding.inflate(getLayoutInflater());

        binding.signInButton.setOnClickListener(v -> verify(v));

        binding.resendVerificationButton.setOnClickListener(v -> resendVerification(v));

        if(getArguments() != null && VerifyEmailArgs.fromBundle(getArguments()).getEmail() != null)
            binding.VerificationEmailEdit.setText(VerifyEmailArgs.fromBundle(getArguments()).getEmail());

        return binding.getRoot();
    }
}
