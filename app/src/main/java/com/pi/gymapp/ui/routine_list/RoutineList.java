package com.pi.gymapp.ui.routine_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.pi.gymapp.R;

public class RoutineList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routine_list);

        String filter = getIntent().getStringExtra("filter");

        if(filter.equals("all"))
            getSupportActionBar().setTitle(R.string.allRoutinesTitle);
        else if(filter.equals("favourites"))
            getSupportActionBar().setTitle(R.string.favRoutinesTitle);
        else
            throw new RuntimeException("Illegal filter");
    }
}