package com.pi.gymapp;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.pi.gymapp.api.ApiCategoryService;
import com.pi.gymapp.api.ApiCycleService;
import com.pi.gymapp.api.ApiExerciseService;
import com.pi.gymapp.api.ApiRoutineService;
import com.pi.gymapp.api.ApiUserService;
import com.pi.gymapp.api.utils.ApiClient;
import com.pi.gymapp.db.AppDatabase;
import com.pi.gymapp.repo.CategoryRepository;
import com.pi.gymapp.repo.CycleRepository;
import com.pi.gymapp.repo.ExerciseRepository;
import com.pi.gymapp.repo.RoutineRepository;
import com.pi.gymapp.repo.UserRepository;
import com.pi.gymapp.utils.AppExecutors;
import com.pi.gymapp.utils.Constants;

public class MyApplication extends Application {

    AppExecutors appExecutors;
    AppPreferences preferences;

    RoutineRepository routineRepository;
    CycleRepository cycleRepository;
    ExerciseRepository exerciseRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;

    public AppPreferences getPreferences() {
        return preferences;
    }

    public RoutineRepository getRoutineRepository() {
        return routineRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public CycleRepository getCycleRepository() {
        return cycleRepository;
    }

    public ExerciseRepository getExerciseRepository() {
        return exerciseRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public AppExecutors getAppExecutors(){
        return appExecutors;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = new AppPreferences(this);

        appExecutors = new AppExecutors();

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, Constants.DATABASE_NAME).build();

        ApiRoutineService routineService = ApiClient.create(this, ApiRoutineService.class);

        ApiCycleService cycleService = ApiClient.create(this, ApiCycleService.class);

        ApiExerciseService exerciseService = ApiClient.create(this, ApiExerciseService.class);

        ApiUserService userService = ApiClient.create(this, ApiUserService.class);

        ApiCategoryService categoryService = ApiClient.create(this, ApiCategoryService.class);

        routineRepository = new RoutineRepository(appExecutors, routineService, database);

        cycleRepository = new CycleRepository(appExecutors, cycleService, database);

        exerciseRepository = new ExerciseRepository(appExecutors, exerciseService, database);

        userRepository = new UserRepository(appExecutors, userService, database);

        categoryRepository = new CategoryRepository(appExecutors, categoryService, database);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

}
