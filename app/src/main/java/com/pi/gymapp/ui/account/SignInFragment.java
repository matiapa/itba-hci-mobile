package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;

import com.pi.gymapp.api.models.Error;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.AppPreferences;
import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.databinding.SignInBinding;
import com.pi.gymapp.ui.MainActivity;
import com.pi.gymapp.utils.Resource;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SignInFragment extends Fragment {
    SignInBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = SignInBinding.inflate(getLayoutInflater());

        binding.signInButton.setOnClickListener(view -> {

            binding.loading.setVisibility(View.VISIBLE);
            binding.signInButton.setEnabled(false);
            binding.signUpButton.setEnabled(false);

            ApiUserService userService= ApiClient.create(getActivity(), ApiUserService.class);
            EditText user_input = binding.usernameSignin;
            EditText password_input = binding.passwordSignin;

            Credentials credentials = new Credentials(user_input.getText().toString(), password_input.getText().toString());
            userService.login(credentials).observe(getViewLifecycleOwner(), r -> {
                binding.loading.setVisibility(View.GONE);
                binding.signInButton.setEnabled(true);
                binding.signUpButton.setEnabled(true);

                if (r.getError() != null) {

                    Error e = r.getError();
                    switch(e.getCode()){
                        case 4:
                            Snackbar.make(view, getContext().getString(R.string.wrong_username_password),
                                    Snackbar.LENGTH_LONG).show();
                            break;

                        case 8:
                            Navigation.findNavController(view).navigate(
                                SignInFragmentDirections.actionSignInFragmentToVerifyEmail(null)
                            );
                            break;

                        default:
                            Snackbar.make(view, getContext().getString(R.string.unexpected_error), Snackbar.LENGTH_LONG).show();
                            break;
                    }

                } else {

                    AppPreferences preferences = new AppPreferences(getContext());
                    preferences.setAuthToken(r.getData().getToken());

                    Intent intent=new Intent(getContext(), MainActivity.class);
                    intent.putExtra("login",true);

                    startActivity(intent);
                    getActivity().finish();

                }
            });

        });

        binding.signUpButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_signUpFragment1)
        );

        return binding.getRoot();

    }

}
