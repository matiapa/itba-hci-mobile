package com.pi.gymapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pi.gymapp.data.RoutineEntity;

import java.util.Arrays;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<RoutineEntity>> favRoutines;
    private final MutableLiveData<List<RoutineEntity>> allRoutines;

    public HomeViewModel() {
        favRoutines = new MutableLiveData<>();
        allRoutines = new MutableLiveData<>();

        favRoutines.setValue(Arrays.asList(
            new RoutineEntity("Routine 1", 60, 4.5, true),
            new RoutineEntity("Routine 2", 40, 4.9, true),
            new RoutineEntity("Routine 3", 80, 4.1, true)
        ));

        allRoutines.setValue(Arrays.asList(
            new RoutineEntity("Routine 1", 60, 4.5, false),
            new RoutineEntity("Routine 2", 40, 4.9, false),
            new RoutineEntity("Routine 3", 80, 4.1, false)
        ));
    }

    public LiveData<List<RoutineEntity>> getFavRoutines() {
        return favRoutines;
    }

    public LiveData<List<RoutineEntity>> getAllRoutines() {
        return allRoutines;
    }

}