package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.api.ApiClient;
import com.pi.gymapp.api.ApiUserService;

import com.pi.gymapp.api.models.VerifyEmailData;
import com.pi.gymapp.ui.MainActivity;

public class VerifyEmail extends AppCompatActivity {


    public void verify(View view){

        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            throw new IllegalStateException("no estoy recibiendo mis argumentos del otro lado");

        ApiUserService userService= ApiClient.create(getApplication(),ApiUserService.class);
        EditText verification_code =(EditText) findViewById(R.id.VerificationCodeEdit);

        if (verification_code.getText().toString().equals("") )
        {
            Snackbar.make(view, "Invalid Input", Snackbar.LENGTH_LONG).show();
            return;
        }
        userService.verifyEmail(new VerifyEmailData(bundle.getString("email"),verification_code.getText().toString())).observe(this, r-> {
            if (r.getError() != null) {
                Snackbar.make(view, "Ups! Something went wrong", Snackbar.LENGTH_LONG).show();
            } else {

                startActivity(new Intent(this, MainActivity.class));

            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_verify_email);
    }
}
