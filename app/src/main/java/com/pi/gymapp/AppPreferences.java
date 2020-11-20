package com.pi.gymapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.pi.gymapp.R;

public class AppPreferences {
    private final String AUTH_TOKEN = "auth_token";
    private final String USER_ID = "user-id";

    private SharedPreferences sharedPreferences;

    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void setAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN, null);
    }

    public void setUserid(Integer userid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_ID, userid);
        editor.apply();
    }

    public Integer getUserId() {

        return sharedPreferences.getInt(USER_ID, -1);
    }
}

