package com.pi.gymapp.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pi.gymapp.R;

public class SignUpS1 extends AppCompatActivity {

    public void nextStep(View view){
        startActivity(new Intent(this, SignUpS2.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_s1);
    }

}