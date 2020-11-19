package com.pi.gymapp;

import android.app.Application;

import androidx.room.Room;

import com.pi.gymapp.api.ApiExerciseService;
import com.pi.gymapp.api.ApiRoutineService;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.repo.ExerciseRepository;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.Constants;

public class MyApplication extends Application {

    AppExecutors appExecutors;
    AppPreferences preferences;
    RoutineRepository routineRepository;
    ExerciseRepository exerciseRepository;

    public AppPreferences getPreferences() {
        return preferences;
    }

    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }
    public ExerciseRepository getExerciseRepository() {
        return exerciseRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = new AppPreferences(this);

        appExecutors = new AppExecutors();

        ApiRoutineService routineService = ApiClient.create(this, ApiRoutineService.class);

        ApiExerciseService exerciseService = ApiClient.create(this, ApiExerciseService.class);

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, Constants.DATABASE_NAME).build();

        routineRepository = new RoutineRepository(appExecutors, routineService, database);

        exerciseRepository = new ExerciseRepository(appExecutors,exerciseService,database);
    }

}
