package com.pi.gymapp.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.pi.gymapp.R;
import com.pi.gymapp.databinding.NavigatorlayoutBinding;


public class LoginActivity extends AppCompatActivity {
    NavigatorlayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = NavigatorlayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment2);
        NavController navController = navHostFragment.getNavController();

       navController.navigate(R.id.signInFragment);
    }
}
