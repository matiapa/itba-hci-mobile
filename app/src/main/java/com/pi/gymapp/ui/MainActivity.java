package com.pi.gymapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.pi.gymapp.AppPreferences;
import com.pi.gymapp.R;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.databinding.MainActivityBinding;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.ui.routine.AllRoutinesFragmentDirections;
import com.pi.gymapp.ui.routine.RoutineDetailFragmentArgs;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private boolean loggedin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Intent Mainintent = getIntent();

        if (Mainintent.getExtras()!=null){
            if (Mainintent.getExtras().containsKey("login")){
                if (Mainintent.getExtras().get("login").equals(true)){
                    loggedin=true;
                }
            }
        }


        if (!loggedin){
            Intent intent = new Intent(this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            finish();
            startActivity(intent);
            return;


        }
        else {

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

            binding.navView.getMenu().findItem(R.id.nav_sign_out).setOnMenuItemClickListener(menuItem -> {
                ApiUserService userService = ApiClient.create(this,ApiUserService.class);

                userService.logout().observe(this,r->{
                    if (r.getError()!= null){
                        Log.d("UI","Logout not successfull");

                       // Snackbar.make( findViewById(R.id.),"Ups! Something went wrong", Snackbar.LENGTH_LONG).show();
                    }else {
                        Log.d("UI","Logout successfull");
                        AppPreferences preferences = new AppPreferences(this);
                        preferences.setAuthToken(null);

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        loggedin=false;

                    }
                });
                return true;
            });


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showProgressBar() {
        binding.loading.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        binding.loading.setVisibility(View.GONE);
    }
}