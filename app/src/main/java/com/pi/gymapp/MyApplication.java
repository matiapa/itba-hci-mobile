package com.pi.gymapp;

import android.app.Application;

import androidx.room.Room;

import com.pi.gymapp.api.ApiRoutineService;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.Constants;

public class MyApplication extends Application {

    AppExecutors appExecutors;
    AppPreferences preferences;
    RoutineRepository routineRepository;

    public AppPreferences getPreferences() {
        return preferences;
    }

    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = new AppPreferences(this);

        appExecutors = new AppExecutors();

        ApiRoutineService routineService = ApiClient.create(this, ApiRoutineService.class);

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, Constants.DATABASE_NAME).build();

        routineRepository = new RoutineRepository(appExecutors, routineService, database);
    }

}
