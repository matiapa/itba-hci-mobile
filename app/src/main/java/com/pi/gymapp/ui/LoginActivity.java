package com.pi.gymapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.pi.gymapp.R;
import com.pi.gymapp.databinding.MainActivityBinding;
import com.pi.gymapp.databinding.NavigatorlayoutBinding;
import com.pi.gymapp.ui.account.SignInFragment;

public class LoginActivity extends AppCompatActivity {
    NavigatorlayoutBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =NavigatorlayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment2);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();


       navController.navigate(R.id.signInFragment);



    }




    public void showProgressBar() {
        binding.loading.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        binding.loading.setVisibility(View.GONE);
    }
}
