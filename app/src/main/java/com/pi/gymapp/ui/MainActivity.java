package com.pi.gymapp.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pi.gymapp.AppPreferences;
import com.pi.gymapp.R;
import com.pi.gymapp.api.models.Error;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.databinding.MainActivityBinding;
import com.pi.gymapp.api.ApiUserService;

import com.pi.gymapp.ui.routine.RoutinesExploreFragmentDirections;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    private boolean darkMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApiUserService userService = ApiClient.create(this, ApiUserService.class);

        AppPreferences appPreferences = new AppPreferences(getApplicationContext());
        if (appPreferences.getAuthToken()!=null)
            userService.getCurrentUser().observe(this, r -> {
                if (r.getError() != null) {

                    Error e = r.getError();
                    if (e.getCode() == 7)
                        login();

                }
            });
        else
            login();

        binding = MainActivityBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_help)
                .setDrawerLayout(binding.drawerLayout)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        Intent mainIntent = getIntent();
        Uri data = mainIntent.getData();

        if (data != null && data.getLastPathSegment() != null) {
            RoutinesExploreFragmentDirections.ActionNavHomeToRoutineDetailFragment navAction =
                    RoutinesExploreFragmentDirections.actionNavHomeToRoutineDetailFragment(
                            Integer.parseInt(data.getLastPathSegment())
                    );

            navController.navigate(navAction.setRoutineId(Integer.parseInt(data.getLastPathSegment())));
        }

        binding.navView.getMenu().findItem(R.id.nav_rate_us).setOnMenuItemClickListener(menuItem -> {
            Intent rateIntent = new Intent(
                    "android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps")
            );

            startActivity(rateIntent);

            return true;
        });

        binding.navView.getMenu().findItem(R.id.nav_sign_out).setOnMenuItemClickListener(menuItem -> {

            userService.logout().observe(this, r -> {
                if (r.getError() != null) {
                    Log.d("UI", "Logout failed");

                    Toast.makeText(this, getResources().getString(R.string.unexpected_error),
                            Toast.LENGTH_SHORT).show();
                } else {
                    AppPreferences preferences = new AppPreferences(this);
                    preferences.setAuthToken(null);

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                }
            });

            return true;
        });

        // binding.navView.getMenu().getItem(0).setIcon(R.drawable.baseline_home_white_18dp);

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow,menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.dark_mode);

        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        darkMode = mode == Configuration.UI_MODE_NIGHT_YES;

        checkable.setChecked(darkMode);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.dark_mode:
                darkMode = !darkMode;
                item.setChecked(darkMode);

                if (darkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }


    public void showProgressBar() {
        binding.loading.setVisibility(View.VISIBLE);
    }


    public void hideProgressBar() {
        binding.loading.setVisibility(View.GONE);
    }


    public void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        finish();
        startActivity(intent);
    }

}