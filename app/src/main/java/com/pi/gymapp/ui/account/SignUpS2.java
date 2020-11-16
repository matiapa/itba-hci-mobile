package com.pi.gymapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.pi.gymapp.R;
import com.pi.gymapp.api.ApiClient;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.AppPreferences;
import com.pi.gymapp.api.models.Credentials;
import com.pi.gymapp.api.models.UserData;
import com.pi.gymapp.ui.MainActivity;

public class SignUpS2 extends AppCompatActivity {

    static boolean isNameValid(String name) {
        String regex = "^[a-zA-Z ]+$";
        return name.matches(regex);
    }
    static boolean isPhoneValid(String phone) {
        String regex = "^([+])?[0-9]{8,15}$";
        return phone.matches(regex);
    }
    static boolean isAgeValid(String age) {
        String regex = "^[0-9]+$";
        return age.matches(regex);
    }

    
    public void finishSignUp(View view){

        Bundle bundle = getIntent().getExtras();

        if (bundle==null)
            throw new IllegalArgumentException("no estoy recibiendo mis argumentos del otro lado");
        ApiUserService userService= ApiClient.create(this,ApiUserService.class);
        EditText name =(EditText) findViewById(R.id.signUpName);
        EditText phone = (EditText)findViewById(R.id.signUpPhone);
        EditText age =(EditText) findViewById(R.id.signUpAge);
        String gender =((Spinner)findViewById(R.id.sexSpinner)).getSelectedItem().toString();


        if (!(isNameValid(name.getText().toString()) && isPhoneValid(phone.getText().toString()) && isAgeValid(age.getText().toString())) )
        {
            Snackbar.make(view, "Invalid Input", Snackbar.LENGTH_LONG).show();
            return;
        }
        userService.createUser(new UserData(
                bundle.getString("username"),
                name.getText().toString(),
                gender.toLowerCase(),
                Integer.parseInt(age.getText().toString()),
                bundle.getString("email"),
                phone.getText().toString(),
                null,
                bundle.getString("password")
                )).observe(this, r->{
            if (r.getError()!= null){
                Snackbar.make(view, "Ups! Something went wrong", Snackbar.LENGTH_LONG).show();
            }else {
//        startActivity(new Intent(this, VerifyEmail.class).putExtra("email","test@tes.com"));
                startActivity(new Intent(this, VerifyEmail.class).putExtra("email",bundle.getString("email")));

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_s2);
    }

}