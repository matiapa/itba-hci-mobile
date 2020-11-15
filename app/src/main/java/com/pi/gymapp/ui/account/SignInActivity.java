package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.ui.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

public class SignInActivity extends AppCompatActivity {

    public void signUp(View view){
        startActivity(new Intent(this, SignUpS1.class));
    }

    public void signIn(View view){

        // API sign in
        if(Math.random() > 0.05)
            startActivity(new Intent(this, MainActivity.class));
        else
            Snackbar.make(view, "Ups! Something went wrong", Snackbar.LENGTH_LONG).show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        // API check sign in
        if(false)
            startActivity(new Intent(this, MainActivity.class));

    }

}
