package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;

import com.pi.gymapp.api.ApiClient;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.AppPreferences;
import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.ui.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {

    public void signUp(View view){
        startActivity(new Intent(this, SignUpS1.class));
    }

    public void signIn(View view){


        ApiUserService userService= ApiClient.create(this,ApiUserService.class);
        EditText user_input =(EditText) findViewById(R.id.username_signin);
        EditText password_input =(EditText) findViewById(R.id.password_signin);

        userService.login(new Credentials(user_input.getText().toString(),password_input.getText().toString())).observe(this,r->{
            if (r.getError()!= null){

                Snackbar.make(view, "Ups! Something went wrong", Snackbar.LENGTH_LONG).show();
            }else {
                Log.d("UI","Token: "+ r.getData().getToken());
                AppPreferences preferences = new AppPreferences(this);
                preferences.setAuthToken(r.getData().getToken());

                startActivity(new Intent(this, MainActivity.class));

            }
        });



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // API check sign in ?
        if(false)
            startActivity(new Intent(this, MainActivity.class));

    }

}
