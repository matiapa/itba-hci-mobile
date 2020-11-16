package com.pi.gymapp.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.api.ApiClient;
import com.pi.gymapp.api.ApiUserService;

public class SignUpS1 extends AppCompatActivity {

    static boolean isEmailValid(String email) {
        String regex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    static boolean isUsernameValid(String username) {
        return username != null && !username.equals("");
    }
    static boolean isPasswordValid(String pass) {

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        return pass.matches(regex);


    }

    public void nextStep(View view){

        ApiUserService userService= ApiClient.create(this,ApiUserService.class);
        EditText user_input =(EditText) findViewById(R.id.Username_signup);
        EditText email_input =(EditText) findViewById(R.id.Email_signup);
        EditText password_input =(EditText) findViewById(R.id.password_signup);

        if (!(isEmailValid(email_input.getText().toString())&&isUsernameValid(user_input.getText().toString()) &&isPasswordValid(password_input.getText().toString()))){
            Snackbar.make(view, "Invalid Input", Snackbar.LENGTH_LONG).show();
            return;
        }

        startActivity(new Intent(this, SignUpS2.class)
                .putExtra("username",user_input.getText().toString())
                .putExtra("email",email_input.getText().toString())
                .putExtra("password",password_input.getText().toString())

        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_s1);
    }

}