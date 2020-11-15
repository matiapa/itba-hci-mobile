package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.ui.MainActivity;

public class SignUpS2 extends AppCompatActivity {
    
    public void finishSignUp(View view){

        // API sign up
        if(Math.random()>0.05)
            startActivity(new Intent(this, MainActivity.class));
        else
            Snackbar.make(view, "Ups! Something went wrong", Snackbar.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_s2);
    }

}